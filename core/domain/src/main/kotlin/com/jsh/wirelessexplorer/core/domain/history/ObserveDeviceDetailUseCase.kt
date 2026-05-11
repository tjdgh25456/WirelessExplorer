package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import kotlinx.coroutines.flow.Flow

interface ObserveDeviceDetailUseCase {
    operator fun invoke(id: Long): Flow<DeviceHistory?>
}
