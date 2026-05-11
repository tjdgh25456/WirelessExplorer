package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleDevice
import javax.inject.Inject

class GetScannedBleDeviceUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : GetScannedBleDeviceUseCase {
    override operator fun invoke(address: String): BleDevice? =
        bleRepository.getScannedDevice(address)
}
