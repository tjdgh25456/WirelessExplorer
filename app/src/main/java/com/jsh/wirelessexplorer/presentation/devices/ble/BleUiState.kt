package com.jsh.wirelessexplorer.presentation.devices.ble

import com.jsh.wirelessexplorer.domain.model.BleConnectionState
import com.jsh.wirelessexplorer.domain.model.BleDevice

data class BleUiState(
    val isScanning: Boolean = false,
    val devices: List<BleDevice> = emptyList(),
    val connectionState: BleConnectionState = BleConnectionState.Idle,
    val connectedDeviceName: String? = null,
    val error: String? = null,
)
