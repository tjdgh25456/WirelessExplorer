package com.jsh.wirelessexplorer.domain.repository

import com.jsh.wirelessexplorer.domain.model.UwbRangingResult
import kotlinx.coroutines.flow.Flow

interface UwbRepository {
    fun isSupported(): Boolean
    fun startRanging(): Flow<UwbRangingResult>
    fun stopRanging()
}
