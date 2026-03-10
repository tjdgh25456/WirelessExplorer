package com.jsh.wirelessexplorer.domain.model

enum class BleDeviceType(val label: String) {
    AUDIO("Audio"),
    WEARABLE("Wearable"),
    PHONE("Phone"),
    COMPUTER("Computer"),
    PERIPHERAL("Peripheral"),
    UNKNOWN("Unknown"),
}
