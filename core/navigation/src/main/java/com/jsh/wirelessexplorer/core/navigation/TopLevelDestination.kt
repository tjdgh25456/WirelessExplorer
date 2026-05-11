package com.jsh.wirelessexplorer.core.navigation

import androidx.annotation.StringRes
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
import com.jsh.wirelessexplorer.core.navigation.R

enum class TopLevelDestination(
    @StringRes val labelResId: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: Any,
) {
    DASHBOARD(R.string.label_nav_dashboard, Icons.Outlined.Home, Icons.Rounded.Home, DashboardRoute),
    DEVICES(R.string.label_nav_devices, Icons.Outlined.Devices, Icons.Rounded.Devices, DevicesRoute()),
    ANALYZE(R.string.label_nav_analyze, Icons.Outlined.Analytics, Icons.Rounded.Analytics, AnalyzeRoute),
    HISTORY(R.string.label_nav_history, Icons.Outlined.History, Icons.Rounded.History, HistoryRoute),
}
