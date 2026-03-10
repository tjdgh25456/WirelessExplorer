package com.jsh.wirelessexplorer.domain.model

enum class AnalyzePeriod(val displayName: String) {
    TODAY("Today"),
    LAST_7_DAYS("7 Days"),
    LAST_30_DAYS("30 Days"),
    ALL_TIME("All Time");

    /** 해당 기간의 시작 시각(밀리초). ALL_TIME은 0을 반환한다. */
    fun startMillis(): Long {
        val now = System.currentTimeMillis()
        return when (this) {
            TODAY -> now - 24L * 60 * 60 * 1_000
            LAST_7_DAYS -> now - 7L * 24 * 60 * 60 * 1_000
            LAST_30_DAYS -> now - 30L * 24 * 60 * 60 * 1_000
            ALL_TIME -> 0L
        }
    }
}
