package com.jsh.wirelessexplorer.feature.ble

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jsh.wirelessexplorer.feature.ble.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.ui.RequireLocationService
import com.jsh.wirelessexplorer.core.ui.RequirePermissions
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.Error
import com.jsh.wirelessexplorer.core.designsystem.theme.OnPrimary
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.BleDeviceType
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme

@Composable
fun BleScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToBleDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BleViewModel = hiltViewModel(),
) {
    RequirePermissions(
        permissions = listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        rationaleTitle = stringResource(R.string.title_ble_permission),
        rationaleMessage = stringResource(R.string.desc_ble_permission),
    ) {
        RequireLocationService {
            BleScreenContent(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToBleDetail = onNavigateToBleDetail,
            )
        }
    }
}

@Composable
private fun BleScreenContent(
    modifier: Modifier = Modifier,
    viewModel: BleViewModel,
    onNavigateToBleDetail: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 블루투스 비활성화 다이얼로그 상태
    var showBluetoothDialog by remember { mutableStateOf(false) }

    // 연결 중인 디바이스 주소 추적
    var connectingAddress by remember { mutableStateOf<String?>(null) }

    // 에러 스낵바 표시
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // 연결 상태 변경 시 연결 중 상태 해제
    LaunchedEffect(uiState.connectionState) {
        when (uiState.connectionState) {
            is BleConnectionState.Connected,
            is BleConnectionState.Error,
            is BleConnectionState.Idle,
            -> connectingAddress = null
            else -> { /* 연결 진행 중 */ }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Bluetooth,
                    contentDescription = null,
                    tint = BleBlue,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.title_bluetooth_scanner),
                    color = OnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )

                Spacer(modifier = Modifier.weight(1f))

                // 스캔 진행 인디케이터
                AnimatedVisibility(
                    visible = uiState.isScanning,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = BleBlue,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 디바이스 수 표시
            Text(
                text = if (uiState.isScanning) {
                    stringResource(R.string.label_devices_found_scanning, uiState.devices.size)
                } else if (uiState.devices.isNotEmpty()) {
                    stringResource(R.string.label_devices_count, uiState.devices.size)
                } else {
                    stringResource(R.string.label_tap_scan_hint)
                },
                color = OnSurfaceDim,
                fontSize = 13.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 스캔 버튼
            Button(
                onClick = {
                    if (uiState.isScanning) {
                        viewModel.stopScan()
                    } else {
                        viewModel.startScan()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isScanning) Error.copy(alpha = 0.85f) else Primary,
                    contentColor = OnPrimary,
                ),
            ) {
                Text(
                    text = if (uiState.isScanning) stringResource(R.string.btn_stop_scan) else stringResource(R.string.btn_start_scan),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // 검색 필터 (디바이스가 있을 때만)
            if (uiState.devices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.filterQuery,
                    onValueChange = viewModel::updateFilter,
                    placeholder = { Text(stringResource(R.string.hint_filter_devices), fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = OnSurfaceDim,
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    trailingIcon = {
                        if (uiState.filterQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateFilter("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = OnSurfaceDim,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BleBlue,
                        unfocusedBorderColor = OnSurfaceDim.copy(alpha = 0.3f),
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = BleBlue,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val displayDevices = uiState.filteredDevices

            // 디바이스 목록
            if (displayDevices.isEmpty() && !uiState.isScanning) {
                // 빈 상태
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.BluetoothDisabled,
                            contentDescription = null,
                            tint = OnSurfaceDim,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.empty_no_devices),
                            color = OnSurfaceMedium,
                            fontSize = 15.sp,
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                ) {
                    items(
                        items = displayDevices,
                        key = { it.address },
                    ) { device ->
                        val isConnecting = connectingAddress == device.address
                        BleDeviceCard(
                            device = device,
                            isConnecting = isConnecting,
                            onConnectClick = {
                                connectingAddress = device.address
                                viewModel.stopScan()
                                viewModel.connect(device.address, device.name)
                            },
                            onCardClick = {
                                onNavigateToBleDetail(device.address)
                            },
                        )
                    }
                }
            }
        }

        // 스낵바
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Surface,
                contentColor = OnSurface,
            )
        }
    }

    // GATT 서비스 바텀시트 (연결 완료 시 표시)
    val connectionState = uiState.connectionState
    if (connectionState is BleConnectionState.Connected) {
        GattBottomSheet(
            deviceName = uiState.connectedDeviceName ?: stringResource(R.string.label_unknown_device),
            services = connectionState.services,
            onDismiss = { /* 시트 닫기만, 연결 유지 */ },
            onDisconnect = { viewModel.disconnect() },
            onReadCharacteristic = { serviceUuid, charUuid ->
                viewModel.readCharacteristic(serviceUuid, charUuid)
            },
            onWriteCharacteristic = { serviceUuid, charUuid, value ->
                viewModel.writeCharacteristic(serviceUuid, charUuid, value)
            },
            onNotifyCharacteristic = { serviceUuid, charUuid ->
                viewModel.toggleNotification(serviceUuid, charUuid)
            },
        )
    }

    // 블루투스 비활성화 다이얼로그
    if (showBluetoothDialog) {
        AlertDialog(
            onDismissRequest = { showBluetoothDialog = false },
            containerColor = Surface,
            title = { Text(stringResource(R.string.title_bluetooth_disabled), color = OnSurface) },
            text = {
                Text(
                    stringResource(R.string.desc_bluetooth_disabled),
                    color = OnSurfaceMedium,
                )
            },
            confirmButton = {
                TextButton(onClick = { showBluetoothDialog = false }) {
                    Text(stringResource(R.string.btn_ok), color = BleBlue)
                }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun BleScreenPreview() {
    WirelessExplorerTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Bluetooth,
                        contentDescription = null,
                        tint = BleBlue,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.title_bluetooth_scanner),
                        color = OnSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.label_devices_count, 3),
                    color = OnSurfaceDim,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = OnPrimary,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.btn_start_scan),
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                ) {
                    items(
                        items = listOf(
                            BleDevice(
                                name = "Galaxy Watch 5",
                                address = "AA:BB:CC:DD:EE:01",
                                rssi = -45,
                                deviceType = BleDeviceType.WEARABLE,
                                bondState = BleBondState.BONDED,
                                lastSeen = System.currentTimeMillis(),
                            ),
                            BleDevice(
                                name = "AirPods Pro",
                                address = "AA:BB:CC:DD:EE:02",
                                rssi = -62,
                                deviceType = BleDeviceType.AUDIO,
                                bondState = BleBondState.NONE,
                                lastSeen = System.currentTimeMillis(),
                            ),
                        ),
                        key = { it.address },
                    ) { device ->
                        BleDeviceCard(
                            device = device,
                            isConnecting = false,
                            onConnectClick = {},
                        )
                    }
                }
            }
        }
    }
}
