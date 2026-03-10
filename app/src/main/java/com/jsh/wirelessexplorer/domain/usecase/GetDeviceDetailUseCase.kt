package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import javax.inject.Inject

class GetDeviceDetailUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    suspend operator fun invoke(id: Long): DeviceHistory? =
        repository.getById(id)
}
