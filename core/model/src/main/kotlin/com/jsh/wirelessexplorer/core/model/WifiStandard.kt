package com.jsh.wirelessexplorer.core.model

/**
 * Wi-Fi 표준 (802.11 세대)
 * Android ScanResult.WIFI_STANDARD_* 값과 매핑
 */
enum class WifiStandard(val label: String, val generation: String) {
    LEGACY("802.11a/b/g", "Wi-Fi"),
    WIFI_4("802.11n", "Wi-Fi 4"),
    WIFI_5("802.11ac", "Wi-Fi 5"),
    WIFI_6("802.11ax", "Wi-Fi 6"),
    WIFI_6E("802.11ax", "Wi-Fi 6E"),
    WIFI_7("802.11be", "Wi-Fi 7"),
    UNKNOWN("Unknown", "Unknown");

    companion object {
        // ScanResult.WIFI_STANDARD_* constants
        private const val STANDARD_UNKNOWN = 0
        private const val STANDARD_LEGACY = 1
        private const val STANDARD_11N = 4
        private const val STANDARD_11AC = 5
        private const val STANDARD_11AX = 6
        private const val STANDARD_11BE = 7

        // 6 GHz 대역 시작 주파수 (Wi-Fi 6E/7 구분용)
        private const val FREQ_6_GHZ_THRESHOLD = 5925

        /**
         * Android ScanResult의 wifiStandard 값과 주파수로 Wi-Fi 표준을 판별한다.
         * Wi-Fi 6 + 6 GHz 대역 → Wi-Fi 6E로 분류
         */
        fun from(wifiStandardInt: Int, frequency: Int = 0): WifiStandard = when (wifiStandardInt) {
            STANDARD_11BE -> WIFI_7
            STANDARD_11AX -> if (frequency >= FREQ_6_GHZ_THRESHOLD) WIFI_6E else WIFI_6
            STANDARD_11AC -> WIFI_5
            STANDARD_11N -> WIFI_4
            STANDARD_LEGACY -> LEGACY
            else -> UNKNOWN
        }
    }
}
