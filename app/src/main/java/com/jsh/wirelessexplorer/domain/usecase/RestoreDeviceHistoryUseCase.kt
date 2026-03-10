package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import javax.inject.Inject

class RestoreDeviceHistoryUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    /** 삭제된 디바이스를 복원한다. */
    suspend operator fun invoke(device: DeviceHistory) {
        repository.upsert(device)
    }
}
