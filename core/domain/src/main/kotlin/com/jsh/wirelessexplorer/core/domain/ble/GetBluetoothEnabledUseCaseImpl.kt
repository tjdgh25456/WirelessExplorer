package com.jsh.wirelessexplorer.core.domain.ble

import javax.inject.Inject

class GetBluetoothEnabledUseCaseImpl @Inject constructor(
    private val bleRepository: BleRepository,
) : GetBluetoothEnabledUseCase {
    override operator fun invoke(): Boolean = bleRepository.isBluetoothEnabled()
}
