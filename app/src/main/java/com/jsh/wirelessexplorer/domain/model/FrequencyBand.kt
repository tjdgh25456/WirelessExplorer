package com.jsh.wirelessexplorer.domain.model

private const val FREQ_2_4_GHZ_START = 2412
private const val FREQ_2_4_GHZ_END = 2484
private const val FREQ_5_GHZ_START = 5170
private const val FREQ_5_GHZ_END = 5825
private const val FREQ_6_GHZ_START = 5925
private const val FREQ_6_GHZ_END = 7125

enum class FrequencyBand(val label: String) {
    BAND_2_4_GHZ("2.4 GHz"),
    BAND_5_GHZ("5 GHz"),
    BAND_6_GHZ("6 GHz");

    companion object {
        /**
         * 주파수(MHz) 값으로 대역을 판별한다.
         * Wi-Fi 6E/7: 5925~7125 MHz → 6 GHz 대역
         */
        fun from(frequency: Int): FrequencyBand = when (frequency) {
            in FREQ_2_4_GHZ_START..FREQ_2_4_GHZ_END -> BAND_2_4_GHZ
            in FREQ_5_GHZ_START..FREQ_5_GHZ_END -> BAND_5_GHZ
            in FREQ_6_GHZ_START..FREQ_6_GHZ_END -> BAND_6_GHZ
            else -> BAND_6_GHZ // 7125+ MHz도 6 GHz 대역으로 분류
        }
    }
}
