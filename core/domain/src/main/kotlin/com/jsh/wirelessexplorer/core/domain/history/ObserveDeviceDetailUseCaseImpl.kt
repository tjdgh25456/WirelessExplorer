package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDeviceDetailUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : ObserveDeviceDetailUseCase {
    override operator fun invoke(id: Long): Flow<DeviceHistory?> = repository.observeById(id)
}
