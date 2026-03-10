package com.jsh.wirelessexplorer.presentation.detail

import com.jsh.wirelessexplorer.domain.model.DeviceHistory

data class DeviceDetailUiState(
    val device: DeviceHistory? = null,
    val isLoading: Boolean = true,
)
