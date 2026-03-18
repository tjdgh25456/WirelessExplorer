package com.jsh.wirelessexplorer.core.domain.history

import com.jsh.wirelessexplorer.core.model.DeviceHistory
import kotlinx.coroutines.flow.Flow

interface SearchDeviceHistoryUseCase {
    operator fun invoke(query: String): Flow<List<DeviceHistory>>
}
