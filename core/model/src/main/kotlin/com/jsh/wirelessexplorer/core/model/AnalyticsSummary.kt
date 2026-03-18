package com.jsh.wirelessexplorer.core.model

data class AnalyticsSummary(
    val deviceCount: Int,
    val totalConnections: Int,
    val bestSignal: Int?,
)
