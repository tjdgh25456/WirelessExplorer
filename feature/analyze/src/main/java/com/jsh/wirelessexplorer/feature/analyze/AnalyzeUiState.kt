package com.jsh.wirelessexplorer.feature.analyze

import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import com.jsh.wirelessexplorer.core.model.AnalyzePeriod
import com.jsh.wirelessexplorer.core.model.TechDistribution
import com.jsh.wirelessexplorer.core.model.TopDevice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AnalyzeUiState(
    val summary: AnalyticsSummary = AnalyticsSummary(0, 0, null),
    val techDistribution: ImmutableList<TechDistribution> = persistentListOf(),
    val topDevices: ImmutableList<TopDevice> = persistentListOf(),
    val selectedPeriod: AnalyzePeriod = AnalyzePeriod.ALL_TIME,
    val isLoading: Boolean = true,
)
