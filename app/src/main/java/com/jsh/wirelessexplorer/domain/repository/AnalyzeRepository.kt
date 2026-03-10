package com.jsh.wirelessexplorer.domain.repository

import com.jsh.wirelessexplorer.domain.model.AnalyticsSummary
import com.jsh.wirelessexplorer.domain.model.TechDistribution
import com.jsh.wirelessexplorer.domain.model.TopDevice
import kotlinx.coroutines.flow.Flow

interface AnalyzeRepository {
    fun observeSummary(): Flow<AnalyticsSummary>
    fun observeTechDistribution(): Flow<List<TechDistribution>>
    fun observeTopDevices(limit: Int = 5): Flow<List<TopDevice>>
}
