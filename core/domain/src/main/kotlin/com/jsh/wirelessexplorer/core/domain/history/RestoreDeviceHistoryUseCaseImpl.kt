package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import javax.inject.Inject

class RestoreDeviceHistoryUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : RestoreDeviceHistoryUseCase {
    /** 삭제된 디바이스를 복원한다. */
    override suspend operator fun invoke(device: DeviceHistory) {
        repository.upsert(device)
    }
}
