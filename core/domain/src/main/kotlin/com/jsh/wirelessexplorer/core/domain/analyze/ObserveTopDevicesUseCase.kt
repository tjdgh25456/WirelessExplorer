package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.TopDevice
import kotlinx.coroutines.flow.Flow

interface ObserveTopDevicesUseCase {
    operator fun invoke(limit: Int = 5): Flow<List<TopDevice>>
}
