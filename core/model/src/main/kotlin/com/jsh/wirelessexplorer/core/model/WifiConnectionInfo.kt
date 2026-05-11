package com.jsh.wirelessexplorer.core.model

data class WifiConnectionInfo(
    val ssid: String,
    val bssid: String,
    val rssi: Int,
    val linkSpeed: Int,
    val frequency: Int,
    val ipAddress: String,
    val channel: Int,
    val securityType: WifiSecurityType,
    val wifiStandard: WifiStandard = WifiStandard.UNKNOWN,
    val channelWidthMhz: Int? = null,
)
