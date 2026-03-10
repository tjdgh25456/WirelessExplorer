package com.jsh.wirelessexplorer.domain.model

data class BleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val deviceType: BleDeviceType,
    val bondState: BleBondState,
    val lastSeen: Long,
)
