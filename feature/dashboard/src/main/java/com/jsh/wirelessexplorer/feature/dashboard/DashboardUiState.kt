package com.jsh.wirelessexplorer.feature.dashboard

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiStatus

data class DashboardUiState(
    val wifiStatus: WifiStatus? = null,
    val bleConnectedDevices: List<BleConnectedDevice> = emptyList(),
    val trafficSpeed: TrafficSpeed = TrafficSpeed(),
    val stats: DashboardStats = DashboardStats(),
    val isLoading: Boolean = true,
)
