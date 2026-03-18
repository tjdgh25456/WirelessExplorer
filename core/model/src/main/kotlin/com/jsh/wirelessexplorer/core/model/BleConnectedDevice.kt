package com.jsh.wirelessexplorer.core.model

data class BleConnectedDevice(
    val address: String,
    val name: String?,
    val rssi: Int = 0,
    val serviceCount: Int = 0,
)
