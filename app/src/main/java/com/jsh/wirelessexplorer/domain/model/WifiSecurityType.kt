package com.jsh.wirelessexplorer.domain.model

enum class WifiSecurityType(val label: String) {
    WPA3("WPA3"),
    WPA2("WPA2"),
    WPA("WPA"),
    WEP("WEP"),
    OPEN("Open");

    companion object {
        /** capabilities 문자열에서 보안 유형을 판별한다. 순서 중요: 상위 프로토콜 우선 매칭 */
        fun from(capabilities: String): WifiSecurityType = when {
            capabilities.contains("WPA3", ignoreCase = true) -> WPA3
            capabilities.contains("WPA2", ignoreCase = true) -> WPA2
            capabilities.contains("WPA", ignoreCase = true) -> WPA
            capabilities.contains("WEP", ignoreCase = true) -> WEP
            else -> OPEN
        }
    }
}
