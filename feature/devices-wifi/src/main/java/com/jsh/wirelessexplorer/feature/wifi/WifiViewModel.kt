package com.jsh.wirelessexplorer.feature.wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.GetWifiEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiConnectionUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ScanWifiNetworksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WifiViewModel @Inject constructor(
    private val scanWifiNetworks: ScanWifiNetworksUseCase,
    private val observeWifiConnection: ObserveWifiConnectionUseCase,
    private val observeTrafficSpeed: ObserveTrafficSpeedUseCase,
    private val getWifiEnabled: GetWifiEnabledUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WifiUiState())
    val uiState: StateFlow<WifiUiState> = _uiState.asStateFlow()

    private var scanJob: Job? = null
    private var connectionJob: Job? = null
    private var speedJob: Job? = null

    init {
        loadConnectionInfo()
        observeSpeed()
    }

    fun startScan() {
        if (!getWifiEnabled()) {
            _uiState.update { it.copy(error = "WIFI_DISABLED") }
            return
        }

        // 기존 스캔 Job 취소 후 새로 시작 (중복 구독 방지)
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            scanWifiNetworks()
                .onStart {
                    _uiState.update { it.copy(isScanning = true, error = null, networks = persistentListOf()) }
                }
                .onCompletion {
                    _uiState.update { it.copy(isScanning = false) }
                }
                .catch { throwable ->
                    Timber.e(throwable, "Wi-Fi 스캔 오류")
                    _uiState.update {
                        it.copy(isScanning = false, error = throwable.message)
                    }
                }
                .collect { networks ->
                    _uiState.update { it.copy(networks = networks.toImmutableList()) }
                }
        }
    }

    fun refresh() {
        loadConnectionInfo()
        startScan()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun observeSpeed() {
        speedJob?.cancel()
        speedJob = observeTrafficSpeed()
            .onEach { speed -> _uiState.update { it.copy(trafficSpeed = speed) } }
            .catch { Timber.e(it, "트래픽 속도 관찰 오류") }
            .launchIn(viewModelScope)
    }

    private fun loadConnectionInfo() {
        // 기존 연결 정보 Job 취소 후 새로 시작 (중복 구독 방지)
        connectionJob?.cancel()
        connectionJob = viewModelScope.launch {
            observeWifiConnection()
                .catch { throwable ->
                    Timber.e(throwable, "연결 정보 조회 오류")
                }
                .collect { info ->
                    _uiState.update { it.copy(connectionInfo = info) }
                }
        }
    }
}
