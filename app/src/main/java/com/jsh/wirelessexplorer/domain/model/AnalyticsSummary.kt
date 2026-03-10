package com.jsh.wirelessexplorer.domain.model

data class AnalyticsSummary(
    val deviceCount: Int,
    val totalConnections: Int,
    val bestSignal: Int?,
)
