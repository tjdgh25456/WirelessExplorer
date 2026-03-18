package com.jsh.wirelessexplorer.core.model

data class GattServiceInfo(
    val uuid: String,
    val name: String,
    val characteristics: List<GattCharacteristicInfo>,
)
