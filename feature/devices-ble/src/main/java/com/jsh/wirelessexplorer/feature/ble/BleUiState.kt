package com.jsh.wirelessexplorer.feature.ble

import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class BleUiState(
    val isScanning: Boolean = false,
    val devices: ImmutableList<BleDevice> = persistentListOf(),
    val filterQuery: String = "",
    val connectionState: BleConnectionState = BleConnectionState.Idle,
    val connectedDeviceName: String? = null,
    val error: String? = null,
) {
    val filteredDevices: ImmutableList<BleDevice>
        get() = if (filterQuery.isBlank()) {
            devices
        } else {
            devices.filter { device ->
                device.name.contains(filterQuery, ignoreCase = true) ||
                    device.address.contains(filterQuery, ignoreCase = true)
            }.toImmutableList()
        }
}
