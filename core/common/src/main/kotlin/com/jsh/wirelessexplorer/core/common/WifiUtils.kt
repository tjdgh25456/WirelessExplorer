package com.jsh.wirelessexplorer.core.common

// Wi-Fi 2.4 GHz 대역 주파수 범위 (MHz)
private const val FREQ_2_4_GHZ_START = 2412
private const val FREQ_2_4_GHZ_END = 2484
private const val FREQ_2_4_GHZ_BASE = 2412
private const val FREQ_2_4_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_2_4_GHZ_FIRST_CHANNEL = 1

// Wi-Fi 5 GHz 대역 주파수 범위 (MHz)
private const val FREQ_5_GHZ_START = 5170
private const val FREQ_5_GHZ_END = 5825
private const val FREQ_5_GHZ_BASE = 5170
private const val FREQ_5_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_5_GHZ_FIRST_CHANNEL = 34

// Wi-Fi 6 GHz 대역 주파수 범위 (MHz)
private const val FREQ_6_GHZ_START = 5925
private const val FREQ_6_GHZ_BASE = 5955
private const val FREQ_6_GHZ_CHANNEL_WIDTH = 5
private const val FREQ_6_GHZ_FIRST_CHANNEL = 1

// IP 주소 변환용 비트마스크/시프트
private const val IP_BYTE_MASK = 0xFF
private const val IP_SHIFT_SECOND_OCTET = 8
private const val IP_SHIFT_THIRD_OCTET = 16
private const val IP_SHIFT_FOURTH_OCTET = 24

fun frequencyToChannel(freq: Int): Int = when {
    freq in FREQ_2_4_GHZ_START..FREQ_2_4_GHZ_END ->
        (freq - FREQ_2_4_GHZ_BASE) / FREQ_2_4_GHZ_CHANNEL_WIDTH + FREQ_2_4_GHZ_FIRST_CHANNEL
    freq in FREQ_5_GHZ_START..FREQ_5_GHZ_END ->
        (freq - FREQ_5_GHZ_BASE) / FREQ_5_GHZ_CHANNEL_WIDTH + FREQ_5_GHZ_FIRST_CHANNEL
    freq >= FREQ_6_GHZ_START ->
        (freq - FREQ_6_GHZ_BASE) / FREQ_6_GHZ_CHANNEL_WIDTH + FREQ_6_GHZ_FIRST_CHANNEL
    else -> 0
}

fun formatIpAddress(ip: Int): String =
    "${ip and IP_BYTE_MASK}.${ip shr IP_SHIFT_SECOND_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_THIRD_OCTET and IP_BYTE_MASK}.${ip shr IP_SHIFT_FOURTH_OCTET and IP_BYTE_MASK}"
