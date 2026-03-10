package com.jsh.wirelessexplorer.data.repository

import com.jsh.wirelessexplorer.data.datasource.UwbRanger
import com.jsh.wirelessexplorer.domain.model.UwbRangingResult
import com.jsh.wirelessexplorer.domain.repository.UwbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UwbRepositoryImpl @Inject constructor(
    private val uwbRanger: UwbRanger,
) : UwbRepository {

    override fun isSupported(): Boolean = uwbRanger.isSupported()

    override fun startRanging(): Flow<UwbRangingResult> = uwbRanger.startRanging()

    override fun stopRanging() = uwbRanger.stopRanging()
}
