package com.jsh.wirelessexplorer.feature.ble.detail

import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice

data class BleDeviceDetailUiState(
    val device: BleDevice? = null,
    val connectionState: BleConnectionState = BleConnectionState.Idle,
    val connectedDeviceName: String? = null,
    val error: String? = null,
)
