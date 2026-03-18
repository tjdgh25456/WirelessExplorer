package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleDevice
import kotlinx.coroutines.flow.Flow

interface ScanBleDevicesUseCase {
    operator fun invoke(): Flow<List<BleDevice>>
}
