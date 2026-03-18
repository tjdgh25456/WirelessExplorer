package com.jsh.wirelessexplorer.core.domain.dashboard

import com.jsh.wirelessexplorer.core.model.DashboardStats
import kotlinx.coroutines.flow.Flow

interface ObserveDashboardStatsUseCase {
    operator fun invoke(): Flow<DashboardStats>
}
