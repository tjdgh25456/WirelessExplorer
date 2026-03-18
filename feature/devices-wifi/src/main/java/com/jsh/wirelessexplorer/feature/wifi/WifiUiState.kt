package com.jsh.wirelessexplorer.feature.wifi

import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class WifiUiState(
    val isScanning: Boolean = false,
    val networks: ImmutableList<WifiNetwork> = persistentListOf(),
    val connectionInfo: WifiConnectionInfo? = null,
    val trafficSpeed: TrafficSpeed = TrafficSpeed(),
    val error: String? = null,
)
