package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleConnectionState
import kotlinx.coroutines.flow.Flow

interface ConnectBleDeviceUseCase {
    operator fun invoke(address: String, deviceName: String): Flow<BleConnectionState>
}
