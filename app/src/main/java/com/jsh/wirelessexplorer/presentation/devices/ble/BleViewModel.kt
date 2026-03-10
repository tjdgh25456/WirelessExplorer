package com.jsh.wirelessexplorer.presentation.devices.ble

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.model.BleConnectionState
import com.jsh.wirelessexplorer.domain.usecase.ConnectBleDeviceUseCase
import com.jsh.wirelessexplorer.domain.usecase.DisconnectBleDeviceUseCase
import com.jsh.wirelessexplorer.domain.usecase.ObserveGattCharacteristicUseCase
import com.jsh.wirelessexplorer.domain.usecase.ReadGattCharacteristicUseCase
import com.jsh.wirelessexplorer.domain.usecase.ScanBleDevicesUseCase
import com.jsh.wirelessexplorer.domain.usecase.WriteGattCharacteristicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val SCAN_TIMEOUT_MS = 60_000L

@HiltViewModel
class BleViewModel @Inject constructor(
    private val scanBleDevices: ScanBleDevicesUseCase,
    private val connectBleDevice: ConnectBleDeviceUseCase,
    private val disconnectBleDevice: DisconnectBleDeviceUseCase,
    private val readGattCharacteristic: ReadGattCharacteristicUseCase,
    private val writeGattCharacteristic: WriteGattCharacteristicUseCase,
    private val observeGattCharacteristic: ObserveGattCharacteristicUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BleUiState())
    val uiState: StateFlow<BleUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null
    private var connectionJob: Job? = null
    private var notifyJob: Job? = null

    fun startScan() {
        if (_uiState.value.isScanning) return

        scanJob?.cancel()
        scanJob = scanBleDevices()
            .onStart {
                Timber.d("BLE 스캔 시작")
                _uiState.update { it.copy(isScanning = true, error = null) }
            }
            .onEach { devices ->
                _uiState.update { it.copy(devices = devices) }
            }
            .onCompletion {
                _uiState.update { it.copy(isScanning = false) }
            }
            .catch { e ->
                Timber.e(e, "BLE 스캔 오류")
                _uiState.update { it.copy(error = e.message, isScanning = false) }
            }
            .launchIn(viewModelScope)

        // 60초 타임아웃 자동 중지
        viewModelScope.launch {
            delay(SCAN_TIMEOUT_MS)
            if (_uiState.value.isScanning) {
                Timber.d("BLE 스캔 타임아웃 (${SCAN_TIMEOUT_MS}ms)")
                stopScan()
            }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _uiState.update { it.copy(isScanning = false) }
    }

    fun connect(address: String, deviceName: String) {
        connectionJob?.cancel()
        connectionJob = connectBleDevice(address, deviceName)
            .onStart {
                Timber.d("BLE 디바이스 연결 시작: $address")
                _uiState.update {
                    it.copy(
                        connectionState = BleConnectionState.Connecting,
                        connectedDeviceName = deviceName,
                        error = null,
                    )
                }
            }
            .onEach { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
            .catch { e ->
                Timber.e(e, "BLE 연결 오류: $address")
                _uiState.update {
                    it.copy(
                        connectionState = BleConnectionState.Error(
                            message = e.message ?: "Connection failed",
                            code = -1,
                        ),
                        error = e.message,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun disconnect() {
        notifyJob?.cancel()
        notifyJob = null
        connectionJob?.cancel()
        connectionJob = null
        disconnectBleDevice()
        _uiState.update {
            it.copy(
                connectionState = BleConnectionState.Idle,
                connectedDeviceName = null,
            )
        }
    }

    fun readCharacteristic(serviceUuid: String, charUuid: String) {
        viewModelScope.launch {
            readGattCharacteristic(serviceUuid, charUuid)
                .onSuccess { value ->
                    Timber.d("Characteristic 읽기 완료: ${value.size} bytes")
                    updateCharacteristicValue(serviceUuid, charUuid, value)
                }
                .onFailure { e ->
                    Timber.e(e, "Characteristic 읽기 실패")
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun writeCharacteristic(serviceUuid: String, charUuid: String, value: ByteArray) {
        viewModelScope.launch {
            writeGattCharacteristic(serviceUuid, charUuid, value)
                .onSuccess {
                    Timber.d("Characteristic 쓰기 완료")
                    // 쓰기 후 값 갱신을 위해 다시 읽기
                    readCharacteristic(serviceUuid, charUuid)
                }
                .onFailure { e ->
                    Timber.e(e, "Characteristic 쓰기 실패")
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun toggleNotification(serviceUuid: String, charUuid: String) {
        notifyJob?.cancel()
        notifyJob = observeGattCharacteristic(serviceUuid, charUuid)
            .onEach { value ->
                Timber.d("Notification 수신: ${value.size} bytes")
                updateCharacteristicValue(serviceUuid, charUuid, value)
            }
            .catch { e ->
                Timber.e(e, "Notification 오류")
                _uiState.update { it.copy(error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Connected 상태의 서비스 목록 내에서 특정 Characteristic 값을 업데이트한다.
     */
    private fun updateCharacteristicValue(
        serviceUuid: String,
        charUuid: String,
        value: ByteArray,
    ) {
        val currentState = _uiState.value.connectionState
        if (currentState !is BleConnectionState.Connected) return

        val updatedServices = currentState.services.map { service ->
            if (service.uuid == serviceUuid) {
                service.copy(
                    characteristics = service.characteristics.map { char ->
                        if (char.uuid == charUuid) char.copy(value = value) else char
                    },
                )
            } else {
                service
            }
        }
        _uiState.update {
            it.copy(connectionState = BleConnectionState.Connected(updatedServices))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
