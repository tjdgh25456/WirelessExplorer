package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDeviceHistoryUseCase @Inject constructor(
    private val repository: DeviceHistoryRepository,
) {
    operator fun invoke(techType: TechType? = null): Flow<List<DeviceHistory>> =
        if (techType != null) repository.observeByTechType(techType)
        else repository.observeAll()
}
