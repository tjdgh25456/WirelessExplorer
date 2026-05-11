package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import javax.inject.Inject

class GetOrCreateDeviceHistoryIdUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : GetOrCreateDeviceHistoryIdUseCase {
    override suspend operator fun invoke(device: DeviceHistory): Result<Long> = runCatching {
        repository.upsert(device)
        repository.getById(device.id)?.id
            ?: error("Failed to retrieve device history after upsert")
    }
}
