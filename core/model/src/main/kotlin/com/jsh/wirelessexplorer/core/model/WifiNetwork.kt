package com.jsh.wirelessexplorer.core.model

data class WifiNetwork(
    val ssid: String,
    val bssid: String,
    val rssi: Int,
    val frequency: Int,
    val securityType: WifiSecurityType,
    val isConnected: Boolean,
    val channelWidth: Int? = null,
    val channelWidthMhz: Int? = null,
    val wifiStandard: WifiStandard = WifiStandard.UNKNOWN,
)
