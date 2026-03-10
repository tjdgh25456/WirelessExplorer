package com.jsh.wirelessexplorer.presentation.devices.uwb

import com.jsh.wirelessexplorer.domain.model.UwbState

data class UwbUiState(
    val state: UwbState = UwbState.Idle,
    val isSupported: Boolean = false,
)
