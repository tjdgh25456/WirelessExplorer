package com.jsh.wirelessexplorer.core.model

enum class AnalyzePeriod(val displayName: String) {
    TODAY("Today"),
    LAST_7_DAYS("7 Days"),
    LAST_30_DAYS("30 Days"),
    ALL_TIME("All Time");

    fun startMillis(now: Long): Long = when (this) {
        TODAY -> now - 24L * 60 * 60 * 1_000
        LAST_7_DAYS -> now - 7L * 24 * 60 * 60 * 1_000
        LAST_30_DAYS -> now - 30L * 24 * 60 * 60 * 1_000
        ALL_TIME -> 0L
    }
}
