package com.jsh.wirelessexplorer.core.model

data class TopDevice(
    val deviceName: String,
    val techType: TechType,
    val connectCount: Int,
)
