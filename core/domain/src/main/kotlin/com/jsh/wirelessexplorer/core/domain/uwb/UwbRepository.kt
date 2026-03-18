package com.jsh.wirelessexplorer.core.domain.uwb

import com.jsh.wirelessexplorer.core.model.UwbRangingResult
import kotlinx.coroutines.flow.Flow

interface UwbRepository {
    fun isSupported(): Boolean
    fun startRanging(): Flow<UwbRangingResult>
    fun stopRanging()
}
