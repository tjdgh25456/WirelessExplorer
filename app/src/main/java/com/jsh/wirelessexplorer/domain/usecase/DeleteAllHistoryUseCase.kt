package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import javax.inject.Inject

class DeleteAllHistoryUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAll()
    }
}
