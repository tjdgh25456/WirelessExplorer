package com.jsh.wirelessexplorer.core.model

sealed interface BleConnectionState {
    data object Idle : BleConnectionState
    data object Scanning : BleConnectionState
    data object Connecting : BleConnectionState
    data object DiscoveringServices : BleConnectionState
    data class Connected(val services: List<GattServiceInfo>) : BleConnectionState
    data object Disconnecting : BleConnectionState
    data class Error(val message: String, val code: Int) : BleConnectionState
}
