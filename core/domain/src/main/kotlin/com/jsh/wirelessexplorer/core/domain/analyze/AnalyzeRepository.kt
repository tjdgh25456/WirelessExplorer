package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.AnalyticsSummary
import com.jsh.wirelessexplorer.core.model.TechDistribution
import com.jsh.wirelessexplorer.core.model.TopDevice
import kotlinx.coroutines.flow.Flow

interface AnalyzeRepository {
    fun observeSummary(): Flow<AnalyticsSummary>
    fun observeTechDistribution(): Flow<List<TechDistribution>>
    fun observeTopDevices(limit: Int = 5): Flow<List<TopDevice>>
}
