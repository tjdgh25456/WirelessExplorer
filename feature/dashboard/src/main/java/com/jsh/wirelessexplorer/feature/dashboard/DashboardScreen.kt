package com.jsh.wirelessexplorer.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.feature.dashboard.R
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.core.model.DashboardStats
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import com.jsh.wirelessexplorer.core.model.WifiStatus
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDevices: () -> Unit,
    onNavigateToWifi: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onNavigateToDevices = onNavigateToDevices,
        onNavigateToWifi = onNavigateToWifi,
        onNavigateToSettings = onNavigateToSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenContent(
    uiState: DashboardUiState,
    onNavigateToDevices: () -> Unit,
    onNavigateToWifi: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_wireless_explorer),
                        style = MaterialTheme.typography.titleLarge,
                        color = OnSurface,
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.cd_settings),
                            tint = OnSurfaceDim,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                ),
            )
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                CircularProgressIndicator(color = Primary)
            }
            return@Scaffold
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            item(key = "spacer_top") {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 연결 상태 카드
            item(key = "connections") {
                val wifiStatus = uiState.wifiStatus
                if (wifiStatus != null && wifiStatus.isConnected) {
                    ConnectionStatusCard(wifiStatus = wifiStatus)
                } else {
                    NotConnectedCard()
                }
            }

            // BLE 연결 기기 (연결된 기기가 있을 때만)
            if (uiState.bleConnectedDevices.isNotEmpty()) {
                item(key = "ble_connected") {
                    BleConnectedCard(devices = uiState.bleConnectedDevices)
                }
            }

            // 통계 행
            item(key = "stats") {
                StatsRow(stats = uiState.stats)
            }

            // 실시간 속도 카드 (Wi-Fi 연결 시)
            val wifiStatus = uiState.wifiStatus
            if (wifiStatus != null && wifiStatus.isConnected) {
                item(key = "speed") {
                    SpeedCardRow(
                        downloadBytesPerSec = uiState.trafficSpeed.downloadBytesPerSec,
                        uploadBytesPerSec = uiState.trafficSpeed.uploadBytesPerSec,
                    )
                }
            }

            // 빠른 실행 버튼
            item(key = "quick_actions") {
                QuickActionRow(
                    onNavigateToDevices = onNavigateToDevices,
                    onNavigateToWifi = onNavigateToWifi,
                )
            }

            item(key = "spacer_bottom") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun DashboardScreenPreview() {
    WirelessExplorerTheme {
        DashboardScreenContent(
            uiState = DashboardUiState(
                isLoading = false,
                wifiStatus = WifiStatus(
                    ssid = "MyNetwork_5G",
                    bssid = "AA:BB:CC:DD:EE:FF",
                    rssi = -45,
                    linkSpeed = 866,
                    frequency = 5200,
                    ipAddress = "192.168.1.100",
                    securityType = WifiSecurityType.WPA2,
                    channel = 40,
                    isConnected = true,
                ),
                stats = DashboardStats(
                    deviceCount = 12,
                    totalConnections = 48,
                    bestSignal = -35,
                ),
            ),
            onNavigateToDevices = {},
            onNavigateToWifi = {},
            onNavigateToSettings = {},
        )
    }
}
