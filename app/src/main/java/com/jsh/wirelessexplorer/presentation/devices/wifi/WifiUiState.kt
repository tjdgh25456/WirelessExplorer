package com.jsh.wirelessexplorer.presentation.devices.wifi

import com.jsh.wirelessexplorer.domain.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.domain.model.WifiNetwork

data class WifiUiState(
    val isScanning: Boolean = false,
    val networks: List<WifiNetwork> = emptyList(),
    val connectionInfo: WifiConnectionInfo? = null,
    val error: String? = null,
)
