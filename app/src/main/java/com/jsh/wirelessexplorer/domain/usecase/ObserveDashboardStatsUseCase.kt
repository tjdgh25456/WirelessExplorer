package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DashboardStats
import com.jsh.wirelessexplorer.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDashboardStatsUseCase @Inject constructor(
    private val repository: DashboardRepository,
) {
    operator fun invoke(): Flow<DashboardStats> = repository.observeStats()
}
