package com.jsh.wirelessexplorer.domain.repository

import com.jsh.wirelessexplorer.domain.model.DashboardStats
import com.jsh.wirelessexplorer.domain.model.WifiStatus
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeWifiStatus(): Flow<WifiStatus?>
    fun observeStats(): Flow<DashboardStats>
}
