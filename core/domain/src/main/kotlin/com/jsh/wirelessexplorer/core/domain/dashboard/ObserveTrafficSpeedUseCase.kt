package com.jsh.wirelessexplorer.core.domain.dashboard

import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import kotlinx.coroutines.flow.Flow

interface ObserveTrafficSpeedUseCase {
    operator fun invoke(): Flow<TrafficSpeed>
}
