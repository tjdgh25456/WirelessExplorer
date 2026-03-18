package com.jsh.wirelessexplorer.core.model

data class DashboardStats(
    val deviceCount: Int = 0,
    val totalConnections: Int = 0,
    val bestSignal: Int? = null,
)
