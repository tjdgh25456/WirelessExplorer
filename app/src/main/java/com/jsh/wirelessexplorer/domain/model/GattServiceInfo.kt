package com.jsh.wirelessexplorer.domain.model

data class GattServiceInfo(
    val uuid: String,
    val name: String,
    val characteristics: List<GattCharacteristicInfo>,
)
