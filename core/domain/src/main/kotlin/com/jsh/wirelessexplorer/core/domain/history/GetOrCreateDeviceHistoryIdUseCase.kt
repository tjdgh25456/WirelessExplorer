package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory

interface GetOrCreateDeviceHistoryIdUseCase {
    suspend operator fun invoke(device: DeviceHistory): Result<Long>
}
