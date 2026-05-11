package com.jsh.wirelessexplorer.feature.ble.model

data class WriteTarget(
    val serviceUuid: String,
    val charUuid: String,
    val charName: String,
)
