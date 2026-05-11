package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleDevice

interface GetScannedBleDeviceUseCase {
    operator fun invoke(address: String): BleDevice?
}
