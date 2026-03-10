package com.jsh.wirelessexplorer.presentation.dashboard

import com.jsh.wirelessexplorer.domain.model.DashboardStats
import com.jsh.wirelessexplorer.domain.model.WifiStatus

data class DashboardUiState(
    val wifiStatus: WifiStatus? = null,
    val stats: DashboardStats = DashboardStats(),
    val isLoading: Boolean = true,
)
