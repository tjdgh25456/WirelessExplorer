package com.jsh.wirelessexplorer.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: Any,
) {
    DASHBOARD("Dashboard", Icons.Outlined.Home, Icons.Rounded.Home, DashboardRoute),
    DEVICES("Devices", Icons.Outlined.Devices, Icons.Rounded.Devices, DevicesRoute),
    ANALYZE("Analyze", Icons.Outlined.Analytics, Icons.Rounded.Analytics, AnalyzeRoute),
    HISTORY("History", Icons.Outlined.History, Icons.Rounded.History, HistoryRoute),
}
