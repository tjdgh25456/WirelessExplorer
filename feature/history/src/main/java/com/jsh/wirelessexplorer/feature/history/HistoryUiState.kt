package com.jsh.wirelessexplorer.feature.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HistoryUiState(
    val devices: ImmutableList<DeviceHistory> = persistentListOf(),
    val selectedFilter: TechType? = null,
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
)
