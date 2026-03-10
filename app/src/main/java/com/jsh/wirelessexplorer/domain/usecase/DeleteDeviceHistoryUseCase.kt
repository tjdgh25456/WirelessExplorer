package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import javax.inject.Inject

class DeleteDeviceHistoryUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    suspend operator fun invoke(device: DeviceHistory) {
        repository.delete(device)
    }
}
