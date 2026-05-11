package com.jsh.wirelessexplorer.core.data.uwb

import com.jsh.wirelessexplorer.core.model.UwbRangingResult
import com.jsh.wirelessexplorer.core.domain.uwb.UwbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UwbRepositoryImpl @Inject constructor(
    private val uwbRanger: UwbRanger,
) : UwbRepository {

    override fun isSupported(): Boolean = uwbRanger.isSupported()

    override fun startRanging(): Flow<UwbRangingResult> = uwbRanger.startRanging()

    override fun stopRanging() = uwbRanger.stopRanging()
}
