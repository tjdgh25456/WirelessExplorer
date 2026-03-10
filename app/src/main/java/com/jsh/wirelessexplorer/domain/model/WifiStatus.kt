package com.jsh.wirelessexplorer.domain.model

data class WifiStatus(
    val ssid: String,
    val rssi: Int,
    val linkSpeed: Int,
    val frequency: Int,
    val ipAddress: String,
    val securityType: WifiSecurityType,
    val channel: Int,
    val isConnected: Boolean,
)
