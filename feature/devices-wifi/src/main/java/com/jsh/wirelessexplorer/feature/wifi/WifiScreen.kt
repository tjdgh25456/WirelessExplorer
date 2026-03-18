package com.jsh.wirelessexplorer.feature.wifi

import android.Manifest
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.feature.wifi.R
import com.jsh.wirelessexplorer.core.ui.RequireLocationService
import com.jsh.wirelessexplorer.core.ui.RequirePermissions
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiNetwork
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme

private const val SCAN_ROTATION_DURATION_MS = 1000

@Composable
fun WifiScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: WifiViewModel = hiltViewModel(),
) {
    RequirePermissions(
        permissions = listOf(
            Manifest.permission.NEARBY_WIFI_DEVICES,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        rationaleTitle = stringResource(R.string.title_wifi_permission),
        rationaleMessage = stringResource(R.string.desc_wifi_permission),
    ) {
        RequireLocationService {
            WifiScreenContent(modifier = modifier, viewModel = viewModel)
        }
    }
}

@Composable
private fun WifiScreenContent(
    modifier: Modifier = Modifier,
    viewModel: WifiViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showWifiOffDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startScan()
    }

    // Wi-Fi 비활성화 다이얼로그
    LaunchedEffect(state.error) {
        if (state.error == "WIFI_DISABLED") {
            showWifiOffDialog = true
            viewModel.clearError()
        }
    }

    if (showWifiOffDialog) {
        WifiOffDialog(onDismiss = { showWifiOffDialog = false })
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // 연결된 네트워크 카드
            state.connectionInfo?.let { info ->
                item(key = "connected") {
                    ConnectedNetworkCard(
                        connectionInfo = info,
                        trafficSpeed = state.trafficSpeed,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // 주변 네트워크 섹션 헤더
            item(key = "header") {
                ScanSectionHeader(
                    count = state.networks.size,
                    isScanning = state.isScanning,
                )
            }

            // 네트워크 목록
            items(
                items = state.networks,
                key = { it.bssid },
            ) { network ->
                WifiNetworkCard(network = network)
            }

            // 빈 상태 표시
            if (state.networks.isEmpty() && !state.isScanning) {
                item(key = "empty") {
                    EmptyState()
                }
            }
        }

        // 새로고침 FAB
        FloatingActionButton(
            onClick = { viewModel.refresh() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            val rotation = if (state.isScanning) {
                val transition = rememberInfiniteTransition(label = "scan_rotation")
                val angle by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = SCAN_ROTATION_DURATION_MS, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                    label = "rotation",
                )
                angle
            } else {
                0f
            }

            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.cd_scan),
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

@Composable
private fun ScanSectionHeader(
    count: Int,
    isScanning: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_nearby_networks_format, count),
            color = OnSurfaceDim,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
        )
        if (isScanning) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = WifiGreen,
                strokeWidth = 2.dp,
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.SignalWifiOff,
            contentDescription = null,
            tint = OnSurfaceDim,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.empty_no_networks),
            color = OnSurfaceDim,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun WifiScreenPreview() {
    WirelessExplorerTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item(key = "connected") {
                    ConnectedNetworkCard(
                        connectionInfo = WifiConnectionInfo(
                            ssid = "MyHome_5G",
                            bssid = "AA:BB:CC:DD:EE:FF",
                            rssi = -42,
                            linkSpeed = 866,
                            frequency = 5200,
                            ipAddress = "192.168.1.100",
                            channel = 40,
                            securityType = WifiSecurityType.WPA2,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item(key = "header") {
                    ScanSectionHeader(count = 3, isScanning = false)
                }
                items(
                    items = listOf(
                        WifiNetwork(
                            ssid = "Office_Network",
                            bssid = "11:22:33:44:55:01",
                            rssi = -55,
                            frequency = 5200,
                            securityType = WifiSecurityType.WPA3,
                            isConnected = false,
                        ),
                        WifiNetwork(
                            ssid = "CafeWiFi",
                            bssid = "11:22:33:44:55:02",
                            rssi = -72,
                            frequency = 2437,
                            securityType = WifiSecurityType.WPA2,
                            isConnected = false,
                        ),
                        WifiNetwork(
                            ssid = "Guest_Open",
                            bssid = "11:22:33:44:55:03",
                            rssi = -85,
                            frequency = 2412,
                            securityType = WifiSecurityType.OPEN,
                            isConnected = false,
                        ),
                    ),
                    key = { it.bssid },
                ) { network ->
                    WifiNetworkCard(network = network)
                }
            }
        }
    }
}

@Composable
private fun WifiOffDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.SignalWifiOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = {
            Text(text = stringResource(R.string.title_wifi_disabled))
        },
        text = {
            Text(text = stringResource(R.string.desc_wifi_disabled))
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_ok))
            }
        },
    )
}
