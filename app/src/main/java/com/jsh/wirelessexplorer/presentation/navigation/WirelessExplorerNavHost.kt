package com.jsh.wirelessexplorer.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

@Composable
fun WirelessExplorerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DashboardRoute,
        modifier = modifier,
    ) {
        composable<DashboardRoute> { PlaceholderScreen("Dashboard") }
        composable<DevicesRoute> { PlaceholderScreen("Devices") }
        composable<AnalyzeRoute> { PlaceholderScreen("Analyze") }
        composable<HistoryRoute> { PlaceholderScreen("History") }
        composable<SettingsRoute> { PlaceholderScreen("Settings") }
        composable<SearchRoute> { PlaceholderScreen("Search") }
        composable<OnboardingRoute> { PlaceholderScreen("Onboarding") }
        composable<DeviceDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DeviceDetailRoute>()
            PlaceholderScreen("Device #${route.deviceId}")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
