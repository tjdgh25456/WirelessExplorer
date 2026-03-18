package com.jsh.wirelessexplorer.core.domain.history

import javax.inject.Inject

class DeleteAllHistoryUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : DeleteAllHistoryUseCase {
    override suspend operator fun invoke() {
        repository.deleteAll()
    }
}
