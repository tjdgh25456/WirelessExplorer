package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.core.domain.ble.ConnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.DisconnectBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.GetScannedBleDeviceUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ObserveGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.ReadGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.domain.ble.WriteGattCharacteristicUseCase
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BleDeviceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getScannedBleDevice: GetScannedBleDeviceUseCase,
    private val connectBleDevice: ConnectBleDeviceUseCase,
    private val disconnectBleDevice: DisconnectBleDeviceUseCase,
    private val readGattCharacteristic: ReadGattCharacteristicUseCase,
    private val writeGattCharacteristic: WriteGattCharacteristicUseCase,
    private val observeGattCharacteristic: ObserveGattCharacteristicUseCase,
) : ViewModel() {

    private val address: String = checkNotNull(savedStateHandle["address"])

    private val _uiState = MutableStateFlow(BleDeviceDetailUiState())
    val uiState: StateFlow<BleDeviceDetailUiState> = _uiState.asStateFlow()

    private var connectionJob: Job? = null
    private var notifyJob: Job? = null

    init {
        loadDevice()
    }

    private fun loadDevice() {
        val device = getScannedBleDevice(address)
        _uiState.update { it.copy(device = device) }
    }

    fun connect() {
        val device = _uiState.value.device ?: return
        connectionJob?.cancel()
        connectionJob = connectBleDevice(device.address, device.name)
            .onStart {
                _uiState.update {
                    it.copy(
                        connectionState = BleConnectionState.Connecting,
                        connectedDeviceName = device.name,
                        error = null,
                    )
                }
            }
            .onEach { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
            .catch { e ->
                Timber.e(e, "BLE 연결 오류: ${device.address}")
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
                    updateCharacteristicValue(serviceUuid, charUuid, value)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun writeCharacteristic(serviceUuid: String, charUuid: String, value: ByteArray) {
        viewModelScope.launch {
            writeGattCharacteristic(serviceUuid, charUuid, value)
                .onSuccess { readCharacteristic(serviceUuid, charUuid) }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun toggleNotification(serviceUuid: String, charUuid: String) {
        notifyJob?.cancel()
        notifyJob = observeGattCharacteristic(serviceUuid, charUuid)
            .onEach { value -> updateCharacteristicValue(serviceUuid, charUuid, value) }
            .catch { e -> _uiState.update { it.copy(error = e.message) } }
            .launchIn(viewModelScope)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

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
        connectionJob?.cancel()
        notifyJob?.cancel()
    }
}
