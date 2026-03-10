package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDeviceHistoryUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    operator fun invoke(query: String): Flow<List<DeviceHistory>> =
        repository.search(query)
}
