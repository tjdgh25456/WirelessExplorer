package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import com.jsh.wirelessexplorer.core.model.TechType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDeviceHistoryUseCaseImpl @Inject constructor(
    private val repository: DeviceHistoryRepository,
) : ObserveDeviceHistoryUseCase {
    override operator fun invoke(techType: TechType?): Flow<List<DeviceHistory>> =
        if (techType != null) repository.observeByTechType(techType)
        else repository.observeAll()
}
