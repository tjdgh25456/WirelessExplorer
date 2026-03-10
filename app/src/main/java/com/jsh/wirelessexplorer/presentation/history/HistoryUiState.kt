package com.jsh.wirelessexplorer.presentation.history

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType

data class HistoryUiState(
    val devices: List<DeviceHistory> = emptyList(),
    val selectedFilter: TechType? = null,
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
)
