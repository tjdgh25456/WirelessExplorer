package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory

interface DeleteDeviceHistoryUseCase {
    suspend operator fun invoke(device: DeviceHistory)
}
