package com.jsh.wirelessexplorer.domain.usecase

import com.jsh.wirelessexplorer.domain.model.BleDevice
import com.jsh.wirelessexplorer.domain.repository.BleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanBleDevicesUseCase @Inject constructor(
    private val bleRepository: BleRepository,
) {
    operator fun invoke(): Flow<List<BleDevice>> = bleRepository.scanDevices()
}
