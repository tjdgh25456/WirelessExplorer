package com.jsh.wirelessexplorer.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.Error
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.UwbViolet
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import com.jsh.wirelessexplorer.presentation.util.RelativeTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    onNavigateBack: () -> Unit,
    onReconnect: (TechType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_device_detail)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                actions = {
                    val device = uiState.device
                    if (device != null) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (device.isFavorite) Icons.Filled.Star
                                else Icons.Outlined.StarBorder,
                                contentDescription = stringResource(R.string.cd_favorite),
                                tint = if (device.isFavorite) Color(0xFFFBBF24) else OnSurface,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnSurface,
                    navigationIconContentColor = OnSurface,
                ),
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            uiState.device == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.empty_device_not_found),
                        color = OnSurfaceDim,
                        fontSize = 16.sp,
                    )
                }
            }

            else -> {
                val device = uiState.device!!
                DeviceDetailContent(
                    device = device,
                    onReconnect = { onReconnect(device.techType) },
                    onDeleteRequest = { showDeleteDialog = true },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }

    // 삭제 확인 다이얼로그
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.dialog_delete_history_title)) },
            text = { Text(stringResource(R.string.dialog_delete_history_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        scope.launch {
                            if (viewModel.deleteDevice()) {
                                onNavigateBack()
                            }
                        }
                    },
                ) {
                    Text(stringResource(R.string.btn_delete), color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            },
            containerColor = Background,
            titleContentColor = OnSurface,
            textContentColor = OnSurfaceDim,
        )
    }
}

@Composable
private fun DeviceDetailContent(
    device: DeviceHistory,
    onReconnect: () -> Unit,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint = device.techType.color()
    val icon = device.techType.icon()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Hero 섹션: 기술 타입 아이콘 + 글로우 이펙트
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(96.dp)
                .drawBehind {
                    // Primary 글로우 효과
                    drawCircle(
                        color = tint.copy(alpha = 0.2f),
                        radius = size.minDimension / 1.5f,
                        center = Offset(size.width / 2, size.height / 2),
                    )
                },
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = device.techType.label,
                    tint = tint,
                    modifier = Modifier.size(40.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = device.deviceName,
            color = OnSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = device.deviceAddress,
            color = OnSurfaceDim,
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 통계 카드 Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DeviceStatCard(
                icon = Icons.Outlined.SignalCellularAlt,
                value = device.rssi?.let { stringResource(R.string.label_dbm_format, it) } ?: stringResource(R.string.label_na),
                label = stringResource(R.string.label_rssi),
                modifier = Modifier.weight(1f),
            )
            DeviceStatCard(
                icon = Icons.Outlined.Link,
                value = device.connectCount.toString(),
                label = stringResource(R.string.label_connections),
                modifier = Modifier.weight(1f),
            )
            DeviceStatCard(
                icon = Icons.Outlined.AccessTime,
                value = RelativeTimeFormatter.formatRelativeTime(device.lastConnectedAt),
                label = stringResource(R.string.label_last_seen),
                modifier = Modifier.weight(1f),
            )
        }

        // BLE GATT 서비스 정보 (extraJson이 있는 경우)
        if (device.techType == TechType.BLE && device.extraJson != null) {
            Spacer(modifier = Modifier.height(24.dp))
            GattServicesSection(extraJson = device.extraJson)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 재연결 버튼
        Button(
            onClick = onReconnect,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
        ) {
            Text(
                text = stringResource(R.string.btn_reconnect),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 삭제 버튼
        OutlinedButton(
            onClick = onDeleteRequest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Error),
            border = androidx.compose.foundation.BorderStroke(1.dp, Error),
        ) {
            Text(
                text = stringResource(R.string.btn_delete_history),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * BLE GATT 서비스 목록을 표시한다.
 * extraJson에 저장된 서비스 정보를 파싱하여 표시.
 */
@Composable
private fun GattServicesSection(
    extraJson: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_gatt_services),
            color = OnSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = extraJson,
            color = OnSurfaceDim,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )
    }
}

private fun TechType.color(): Color = when (this) {
    TechType.BLE -> BleBlue
    TechType.WIFI -> WifiGreen
    TechType.NFC -> NfcAmber
    TechType.UWB -> UwbViolet
}

private fun TechType.icon(): ImageVector = when (this) {
    TechType.BLE -> Icons.Filled.Bluetooth
    TechType.WIFI -> Icons.Filled.Wifi
    TechType.NFC -> Icons.Filled.Nfc
    TechType.UWB -> Icons.Outlined.Sensors
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun DeviceDetailScreenPreview() {
    WirelessExplorerTheme {
        DeviceDetailContent(
            device = DeviceHistory(
                id = 1,
                deviceName = "Galaxy Watch 5",
                deviceAddress = "AA:BB:CC:DD:EE:01",
                techType = TechType.BLE,
                rssi = -45,
                isFavorite = true,
                connectCount = 12,
            ),
            onReconnect = {},
            onDeleteRequest = {},
        )
    }
}
