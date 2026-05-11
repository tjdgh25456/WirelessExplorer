package com.jsh.wirelessexplorer.core.domain.ble

import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import kotlinx.coroutines.flow.Flow

interface ObserveBleConnectionsUseCase {
    operator fun invoke(): Flow<List<BleConnectedDevice>>
}
