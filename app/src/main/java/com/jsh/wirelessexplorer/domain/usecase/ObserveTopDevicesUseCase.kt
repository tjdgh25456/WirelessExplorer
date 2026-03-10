package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.TopDevice
import com.jsh.wirelessexplorer.domain.repository.AnalyzeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTopDevicesUseCase @Inject constructor(
    private val repository: AnalyzeRepository,
) {
    operator fun invoke(limit: Int = DEFAULT_TOP_DEVICES_LIMIT): Flow<List<TopDevice>> =
        repository.observeTopDevices(limit)

    companion object {
        private const val DEFAULT_TOP_DEVICES_LIMIT = 5
    }
}
