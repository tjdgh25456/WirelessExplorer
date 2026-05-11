package com.jsh.wirelessexplorer.core.domain.dashboard

import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiStatus
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeWifiStatus(): Flow<WifiStatus?>
    fun observeStats(): Flow<DashboardStats>
    fun observeTrafficSpeed(): Flow<TrafficSpeed>
}
