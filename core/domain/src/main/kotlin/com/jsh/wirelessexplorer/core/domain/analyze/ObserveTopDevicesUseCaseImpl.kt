package com.jsh.wirelessexplorer.core.domain.analyze

import com.jsh.wirelessexplorer.core.model.TopDevice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTopDevicesUseCaseImpl @Inject constructor(
    private val repository: AnalyzeRepository,
) : ObserveTopDevicesUseCase {
    override operator fun invoke(limit: Int): Flow<List<TopDevice>> =
        repository.observeTopDevices(limit)

    companion object {
        private const val DEFAULT_TOP_DEVICES_LIMIT = 5
    }
}
