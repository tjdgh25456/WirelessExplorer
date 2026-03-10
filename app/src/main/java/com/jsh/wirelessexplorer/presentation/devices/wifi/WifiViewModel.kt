package com.jsh.wirelessexplorer.presentation.devices.wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.repository.WifiRepository
import com.jsh.wirelessexplorer.domain.usecase.ObserveWifiConnectionUseCase
import com.jsh.wirelessexplorer.domain.usecase.ScanWifiNetworksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WifiViewModel @Inject constructor(
    private val scanWifiNetworks: ScanWifiNetworksUseCase,
    private val observeWifiConnection: ObserveWifiConnectionUseCase,
    private val wifiRepository: WifiRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WifiUiState())
    val uiState: StateFlow<WifiUiState> = _uiState.asStateFlow()

    init {
        loadConnectionInfo()
    }

    fun startScan() {
        if (!wifiRepository.isWifiEnabled()) {
            _uiState.update { it.copy(error = "WIFI_DISABLED") }
            return
        }

        viewModelScope.launch {
            scanWifiNetworks()
                .onStart {
                    _uiState.update { it.copy(isScanning = true, error = null) }
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
                    _uiState.update { it.copy(networks = networks) }
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

    private fun loadConnectionInfo() {
        viewModelScope.launch {
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
