package com.jsh.wirelessexplorer.core.domain.dashboard

import com.jsh.wirelessexplorer.core.model.DashboardStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDashboardStatsUseCaseImpl @Inject constructor(
    private val repository: DashboardRepository,
) : ObserveDashboardStatsUseCase {
    override operator fun invoke(): Flow<DashboardStats> = repository.observeStats()
}
