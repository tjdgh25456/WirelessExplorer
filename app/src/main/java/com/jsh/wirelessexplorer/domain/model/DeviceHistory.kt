package com.jsh.wirelessexplorer.domain.model

data class DeviceHistory(
    val id: Long = 0,
    val deviceName: String,
    val deviceAddress: String,
    val techType: TechType,
    val rssi: Int? = null,
    val isFavorite: Boolean = false,
    val connectCount: Int = 1,
    val lastConnectedAt: Long = System.currentTimeMillis(),
    val extraJson: String? = null
)
