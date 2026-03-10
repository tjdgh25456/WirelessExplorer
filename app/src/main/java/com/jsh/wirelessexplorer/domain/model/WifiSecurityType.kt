package com.jsh.wirelessexplorer.domain.model

enum class WifiSecurityType(val label: String) {
    WPA3("WPA3"),
    WPA2("WPA2"),
    WPA("WPA"),
    OPEN("Open");

    companion object {
        /** capabilities 문자열에서 보안 유형을 판별한다. */
        fun from(capabilities: String): WifiSecurityType = when {
            capabilities.contains("WPA3", ignoreCase = true) -> WPA3
            capabilities.contains("WPA2", ignoreCase = true) -> WPA2
            capabilities.contains("WPA", ignoreCase = true) -> WPA
            else -> OPEN
        }
    }
}
