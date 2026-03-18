package com.jsh.wirelessexplorer.core.domain.dashboard

import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTrafficSpeedUseCaseImpl @Inject constructor(
    private val repository: DashboardRepository,
) : ObserveTrafficSpeedUseCase {
    override operator fun invoke(): Flow<TrafficSpeed> = repository.observeTrafficSpeed()
}
