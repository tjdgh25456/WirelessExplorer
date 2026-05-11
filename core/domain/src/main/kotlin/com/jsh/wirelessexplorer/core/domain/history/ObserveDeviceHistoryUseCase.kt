package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.flow.Flow

interface ObserveDeviceHistoryUseCase {
    operator fun invoke(techType: TechType? = null): Flow<List<DeviceHistory>>
}
