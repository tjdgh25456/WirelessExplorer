package com.jsh.wirelessexplorer.core.model

enum class SignalStrength(val minRssi: Int) {
    EXCELLENT(-50),
    GOOD(-70),
    FAIR(-85),
    POOR(Int.MIN_VALUE);

    companion object {
        fun from(rssi: Int): SignalStrength = when {
            rssi >= EXCELLENT.minRssi -> EXCELLENT
            rssi >= GOOD.minRssi -> GOOD
            rssi >= FAIR.minRssi -> FAIR
            else -> POOR
        }
    }
}
