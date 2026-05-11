package com.jsh.wirelessexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jsh.wirelessexplorer.core.navigation.DashboardRoute
import com.jsh.wirelessexplorer.core.navigation.OnboardingRoute
import com.jsh.wirelessexplorer.core.navigation.TopLevelDestination
import com.jsh.wirelessexplorer.presentation.navigation.WirelessExplorerNavHost
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.isLoading }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            WirelessExplorerTheme {
                if (!uiState.isLoading) {
                    val startDestination: Any = if (uiState.isOnboardingCompleted) {
                        DashboardRoute
                    } else {
                        OnboardingRoute
                    }
                    MainScreen(startDestination = startDestination)
                }
            }
        }
    }
}

@Composable
private fun MainScreen(startDestination: Any = DashboardRoute) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        TopLevelDestination.entries.any { dest.hasRoute(it.route::class) }
    } == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Surface) {
                    TopLevelDestination.entries.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(destination.route::class)
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                val label = stringResource(destination.labelResId)
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.icon,
                                    contentDescription = label,
                                )
                            },
                            label = { Text(stringResource(destination.labelResId)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Primary,
                                selectedTextColor = Primary,
                                unselectedIconColor = OnSurfaceDim,
                                unselectedTextColor = OnSurfaceDim,
                                indicatorColor = Primary.copy(alpha = 0.12f),
                            ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        WirelessExplorerNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
