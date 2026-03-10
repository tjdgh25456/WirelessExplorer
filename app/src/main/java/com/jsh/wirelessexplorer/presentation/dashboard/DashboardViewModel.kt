package com.jsh.wirelessexplorer.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.domain.usecase.ObserveDashboardStatsUseCase
import com.jsh.wirelessexplorer.domain.usecase.ObserveWifiStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    observeWifiStatus: ObserveWifiStatusUseCase,
    observeDashboardStats: ObserveDashboardStatsUseCase,
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        observeWifiStatus(),
        observeDashboardStats(),
    ) { wifiStatus, stats ->
        DashboardUiState(
            wifiStatus = wifiStatus,
            stats = stats,
            isLoading = false,
        )
    }.catch { throwable ->
        Timber.e(throwable, "대시보드 상태 수집 오류")
        emit(DashboardUiState(isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState(),
    )
}
