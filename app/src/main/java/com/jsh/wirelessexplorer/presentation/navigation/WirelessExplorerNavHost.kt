package com.jsh.wirelessexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.navigation.AnalyzeRoute
import com.jsh.wirelessexplorer.core.navigation.DashboardRoute
import com.jsh.wirelessexplorer.core.navigation.BleDeviceDetailRoute
import com.jsh.wirelessexplorer.core.navigation.DeviceDetailRoute
import com.jsh.wirelessexplorer.core.navigation.DevicesRoute
import com.jsh.wirelessexplorer.core.navigation.HistoryRoute
import com.jsh.wirelessexplorer.core.navigation.OnboardingRoute
import com.jsh.wirelessexplorer.core.navigation.SearchRoute
import com.jsh.wirelessexplorer.core.navigation.SettingsRoute
import com.jsh.wirelessexplorer.feature.analyze.AnalyzeScreen
import com.jsh.wirelessexplorer.feature.dashboard.DashboardScreen
import com.jsh.wirelessexplorer.feature.ble.detail.BleDeviceDetailScreen
import com.jsh.wirelessexplorer.feature.detail.DeviceDetailScreen
import com.jsh.wirelessexplorer.feature.devices.DevicesScreen
import com.jsh.wirelessexplorer.feature.history.HistoryScreen
import com.jsh.wirelessexplorer.feature.onboarding.OnboardingScreen
import com.jsh.wirelessexplorer.feature.search.SearchScreen
import com.jsh.wirelessexplorer.feature.settings.SettingsScreen

@Composable
fun WirelessExplorerNavHost(
    navController: NavHostController,
    startDestination: Any = DashboardRoute,
    modifier: Modifier = Modifier,
) {
    val navigateToDevicesTab: (Int) -> Unit = { initialTab ->
        navController.navigate(DevicesRoute(initialTab = initialTab)) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<DashboardRoute> {
            DashboardScreen(
                onNavigateToDevices = { navigateToDevicesTab(0) },
                onNavigateToWifi = { navigateToDevicesTab(1) },
                onNavigateToSettings = {
                    navController.navigate(SettingsRoute)
                },
                onNavigateToBleDetail = { address ->
                    navController.navigate(BleDeviceDetailRoute(address))
                },
            )
        }

        composable<DevicesRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DevicesRoute>()
            DevicesScreen(
                initialTab = route.initialTab,
                onNavigateToDetail = { deviceId ->
                    navController.navigate(DeviceDetailRoute(deviceId))
                },
                onNavigateToBleDetail = { address ->
                    navController.navigate(BleDeviceDetailRoute(address))
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
                onNavigateToDevices = { navigateToDevicesTab(0) },
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

        composable<BleDeviceDetailRoute> {
            BleDeviceDetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<DeviceDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DeviceDetailRoute>()
            DeviceDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onReconnect = { techType ->
                    val tabIndex = when (techType) {
                        TechType.BLE -> 0
                        TechType.WIFI -> 1
                        TechType.NFC -> 2
                        TechType.UWB -> 3
                    }
                    navigateToDevicesTab(tabIndex)
                },
            )
        }
    }
}
