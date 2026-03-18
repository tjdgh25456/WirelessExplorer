package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDeviceHistoryUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : SearchDeviceHistoryUseCase {
    override operator fun invoke(query: String): Flow<List<DeviceHistory>> =
        repository.search(query)
}
