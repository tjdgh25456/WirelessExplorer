package com.jsh.wirelessexplorer.feature.detail

import com.jsh.wirelessexplorer.core.model.DeviceHistory

data class DeviceDetailUiState(
    val device: DeviceHistory? = null,
    val isLoading: Boolean = true,
)
