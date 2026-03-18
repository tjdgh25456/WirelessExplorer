package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleDevice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanBleDevicesUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : ScanBleDevicesUseCase {
    override operator fun invoke(): Flow<List<BleDevice>> = bleRepository.scanDevices()
}
