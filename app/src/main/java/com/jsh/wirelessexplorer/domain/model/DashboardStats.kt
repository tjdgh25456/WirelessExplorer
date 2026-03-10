package com.jsh.wirelessexplorer.domain.model

data class DashboardStats(
    val deviceCount: Int = 0,
    val totalConnections: Int = 0,
    val bestSignal: Int? = null,
)
