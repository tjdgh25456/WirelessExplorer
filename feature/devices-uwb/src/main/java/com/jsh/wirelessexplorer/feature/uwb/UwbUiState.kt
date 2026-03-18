package com.jsh.wirelessexplorer.feature.uwb

import com.jsh.wirelessexplorer.core.model.UwbState

data class UwbUiState(
    val state: UwbState = UwbState.Idle,
    val isSupported: Boolean = false,
)
