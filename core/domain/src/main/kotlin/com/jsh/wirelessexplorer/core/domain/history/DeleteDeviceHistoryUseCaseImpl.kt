package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import javax.inject.Inject

class DeleteDeviceHistoryUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : DeleteDeviceHistoryUseCase {
    override suspend operator fun invoke(device: DeviceHistory) {
        repository.delete(device)
    }
}
