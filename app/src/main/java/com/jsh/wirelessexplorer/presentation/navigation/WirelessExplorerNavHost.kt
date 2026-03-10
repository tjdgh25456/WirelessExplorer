package com.jsh.wirelessexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jsh.wirelessexplorer.presentation.analyze.AnalyzeScreen
import com.jsh.wirelessexplorer.presentation.dashboard.DashboardScreen
import com.jsh.wirelessexplorer.presentation.detail.DeviceDetailScreen
import com.jsh.wirelessexplorer.presentation.devices.DevicesScreen
import com.jsh.wirelessexplorer.presentation.history.HistoryScreen
import com.jsh.wirelessexplorer.presentation.onboarding.OnboardingScreen
import com.jsh.wirelessexplorer.presentation.search.SearchScreen
import com.jsh.wirelessexplorer.presentation.settings.SettingsScreen

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
        composable<DashboardRoute> {
            DashboardScreen(
                onNavigateToDevices = {
                    navController.navigate(DevicesRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(SettingsRoute)
                },
            )
        }

        composable<DevicesRoute> {
            DevicesScreen(
                onNavigateToDetail = { deviceId ->
                    navController.navigate(DeviceDetailRoute(deviceId))
                },
            )
        }

        composable<AnalyzeRoute> {
            AnalyzeScreen()
        }

        composable<HistoryRoute> {
            HistoryScreen(
                onNavigateToSearch = {
                    navController.navigate(SearchRoute)
                },
                onNavigateToDetail = { deviceId ->
                    navController.navigate(DeviceDetailRoute(deviceId))
                },
                onNavigateToDevices = {
                    navController.navigate(DevicesRoute) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<SearchRoute> {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { deviceId ->
                    navController.navigate(DeviceDetailRoute(deviceId))
                },
            )
        }

        composable<OnboardingRoute> {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(DashboardRoute) {
                        popUpTo(OnboardingRoute) { inclusive = true }
                    }
                },
            )
        }

        composable<DeviceDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DeviceDetailRoute>()
            DeviceDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onReconnect = { /* TODO: 기술별 재연결 화면으로 이동 */ },
            )
        }
    }
}
