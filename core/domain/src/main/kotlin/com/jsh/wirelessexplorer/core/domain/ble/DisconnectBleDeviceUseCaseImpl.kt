package com.jsh.wirelessexplorer.core.domain.ble

import javax.inject.Inject

class DisconnectBleDeviceUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : DisconnectBleDeviceUseCase {
    override operator fun invoke() = bleRepository.disconnect()
}
