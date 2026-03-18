package com.jsh.wirelessexplorer.feature.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsh.wirelessexplorer.core.model.AnalyzePeriod
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveAnalyticsSummaryUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTechDistributionUseCase
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTopDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
    observeSummary: ObserveAnalyticsSummaryUseCase,
    observeDistribution: ObserveTechDistributionUseCase,
    observeTopDevices: ObserveTopDevicesUseCase,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(AnalyzePeriod.ALL_TIME)

    val uiState: StateFlow<AnalyzeUiState> = combine(
        observeSummary(),
        observeDistribution(),
        observeTopDevices(),
        selectedPeriod,
    ) { summary, distribution, topDevices, period ->
        AnalyzeUiState(
            summary = summary,
            techDistribution = distribution.toImmutableList(),
            topDevices = topDevices.toImmutableList(),
            selectedPeriod = period,
            isLoading = false,
        )
    }.catch { throwable ->
        Timber.e(throwable, "분석 데이터 수집 오류")
        emit(AnalyzeUiState(isLoading = false))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AnalyzeUiState(),
    )

    /** 기간 필터 변경 (향후 쿼리 필터링에 사용) */
    fun selectPeriod(period: AnalyzePeriod) {
        selectedPeriod.value = period
    }
}
