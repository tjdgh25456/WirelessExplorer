package com.jsh.wirelessexplorer.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.core.domain.ble.ObserveBleConnectionsUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveDashboardStatsUseCase
import com.jsh.wirelessexplorer.core.domain.dashboard.ObserveTrafficSpeedUseCase
import com.jsh.wirelessexplorer.core.domain.wifi.ObserveWifiStatusUseCase
import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    observeWifiStatus: ObserveWifiStatusUseCase,
    observeDashboardStats: ObserveDashboardStatsUseCase,
    observeBleConnections: ObserveBleConnectionsUseCase,
    observeTrafficSpeed: ObserveTrafficSpeedUseCase,
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        observeWifiStatus()
            .onStart { emit(null) }
            .catch { Timber.e(it, "WiFi 상태 수집 오류"); emit(null) },
        observeDashboardStats()
            .onStart { emit(DashboardStats()) }
            .catch { Timber.e(it, "통계 수집 오류"); emit(DashboardStats()) },
        observeBleConnections()
            .catch { Timber.e(it, "BLE 연결 수집 오류"); emit(emptyList()) },
        observeTrafficSpeed()
            .onStart { emit(TrafficSpeed()) }
            .catch { Timber.e(it, "트래픽 수집 오류"); emit(TrafficSpeed()) },
    ) { wifiStatus, stats, bleDevices, speed ->
        DashboardUiState(
            wifiStatus = wifiStatus,
            bleConnectedDevices = bleDevices,
            trafficSpeed = speed,
            stats = stats,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )
}
