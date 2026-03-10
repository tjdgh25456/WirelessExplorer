package com.jsh.wirelessexplorer.presentation.analyze

import com.jsh.wirelessexplorer.domain.model.AnalyticsSummary
import com.jsh.wirelessexplorer.domain.model.AnalyzePeriod
import com.jsh.wirelessexplorer.domain.model.TechDistribution
import com.jsh.wirelessexplorer.domain.model.TopDevice

data class AnalyzeUiState(
    val summary: AnalyticsSummary = AnalyticsSummary(0, 0, null),
    val techDistribution: List<TechDistribution> = emptyList(),
    val topDevices: List<TopDevice> = emptyList(),
    val selectedPeriod: AnalyzePeriod = AnalyzePeriod.ALL_TIME,
    val isLoading: Boolean = true,
)
