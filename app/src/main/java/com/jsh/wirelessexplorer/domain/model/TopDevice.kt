package com.jsh.wirelessexplorer.domain.model

data class TopDevice(
    val deviceName: String,
    val techType: TechType,
    val connectCount: Int,
)
