package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.core.designsystem.theme.Background
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.Error
import com.jsh.wirelessexplorer.core.designsystem.theme.NfcAmber
import com.jsh.wirelessexplorer.core.designsystem.theme.OnPrimary
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface as ThemeSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.model.BleBondState
import com.jsh.wirelessexplorer.core.model.BleConnectionState
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.SignalStrength
import com.jsh.wirelessexplorer.feature.ble.R
import com.jsh.wirelessexplorer.feature.ble.SignalBars
import com.jsh.wirelessexplorer.feature.ble.WriteValueDialog

private const val AVATAR_PULSE_DURATION_MS = 2000
private const val GLASS_CARD_BORDER_ALPHA = 0.1f
private const val GLASS_CARD_BG_ALPHA = 0.05f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleDeviceDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BleDeviceDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var writeTarget by remember { mutableStateOf<WriteTarget?>(null) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    val device = uiState.device

    Scaffold(
        modifier = modifier,
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = OnSurface,
                ),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = ThemeSurface,
                    contentColor = OnSurface,
                )
            }
        },
    ) { innerPadding ->
        if (device == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.error_device_not_found),
                    color = OnSurfaceMedium,
                    fontSize = 15.sp,
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            // Hero Section
            DeviceHeroSection(
                device = device,
                connectionState = uiState.connectionState,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Connection Info Card
            ConnectionInfoCard(device = device)

            Spacer(modifier = Modifier.height(16.dp))

            // Advertising Data
            AdvertisingDataSection(device = device)

            Spacer(modifier = Modifier.height(16.dp))

            // GATT Services (연결 시)
            val connectionState = uiState.connectionState
            if (connectionState is BleConnectionState.Connected) {
                GattServicesSection(
                    services = connectionState.services,
                    onReadCharacteristic = viewModel::readCharacteristic,
                    onWriteCharacteristic = { sUuid, cUuid ->
                        val service = connectionState.services.find { it.uuid == sUuid }
                        val char = service?.characteristics?.find { it.uuid == cUuid }
                        writeTarget = WriteTarget(sUuid, cUuid, char?.name ?: cUuid)
                    },
                    onNotifyCharacteristic = viewModel::toggleNotification,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Action Buttons
            ConnectionActionButton(
                connectionState = uiState.connectionState,
                onConnect = viewModel::connect,
                onDisconnect = viewModel::disconnect,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    writeTarget?.let { target ->
        WriteValueDialog(
            characteristicName = target.charName,
            onDismiss = { writeTarget = null },
            onWrite = { value ->
                viewModel.writeCharacteristic(target.serviceUuid, target.charUuid, value)
                writeTarget = null
            },
        )
    }
}

// --- Hero Section ---

@Composable
private fun DeviceHeroSection(
    device: BleDevice,
    connectionState: BleConnectionState,
    modifier: Modifier = Modifier,
) {
    val isConnected = connectionState is BleConnectionState.Connected
    val isConnecting = connectionState is BleConnectionState.Connecting ||
        connectionState is BleConnectionState.DiscoveringServices

    val avatarBorderColor by animateColorAsState(
        targetValue = when {
            isConnected -> WifiGreen
            isConnecting -> BleBlue
            else -> Primary.copy(alpha = 0.3f)
        },
        label = "avatarBorder",
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Avatar
        Box(contentAlignment = Alignment.BottomEnd) {
            // 연결 중 펄스 애니메이션
            if (isConnecting) {
                val transition = rememberInfiniteTransition(label = "pulse")
                val pulseAlpha by transition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(AVATAR_PULSE_DURATION_MS),
                        repeatMode = RepeatMode.Restart,
                    ),
                    label = "pulseAlpha",
                )
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .alpha(pulseAlpha)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(BleBlue.copy(alpha = 0.3f), Color.Transparent),
                            ),
                            shape = CircleShape,
                        ),
                )
            }

            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = Primary.copy(alpha = 0.15f),
                border = BorderStroke(3.dp, avatarBorderColor),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isConnected) {
                            Icons.Default.BluetoothConnected
                        } else {
                            Icons.Default.Bluetooth
                        },
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(36.dp),
                    )
                }
            }

            // 연결 상태 뱃지
            if (isConnected) {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = WifiGreen,
                    border = BorderStroke(2.dp, Background),
                ) {}
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 디바이스 이름
        Text(
            text = device.name,
            color = OnSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // MAC 주소
        Text(
            text = device.address,
            color = OnSurfaceDim,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상태 pill
        StatusPill(connectionState = connectionState)
    }
}

@Composable
private fun StatusPill(
    connectionState: BleConnectionState,
    modifier: Modifier = Modifier,
) {
    val (label, color) = when (connectionState) {
        is BleConnectionState.Connected ->
            stringResource(R.string.label_status_connected) to WifiGreen
        is BleConnectionState.Connecting,
        is BleConnectionState.DiscoveringServices,
        -> stringResource(R.string.label_connecting) to BleBlue
        is BleConnectionState.Error ->
            stringResource(R.string.label_status_error) to Error
        else ->
            stringResource(R.string.label_status_disconnected) to OnSurfaceDim
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Surface(
                modifier = Modifier.size(6.dp),
                shape = CircleShape,
                color = color,
            ) {}
            Text(
                text = label,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// --- Connection Info Card ---

@Composable
private fun ConnectionInfoCard(
    device: BleDevice,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Signal Strength
            InfoRow(
                label = stringResource(R.string.label_detail_signal),
                trailing = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SignalBars(rssi = device.rssi)
                        Text(
                            text = "${device.rssi} dBm",
                            color = OnSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                        )
                        QualityBadge(rssi = device.rssi)
                    }
                },
            )

            InfoDivider()

            // Device Type
            InfoRow(
                label = stringResource(R.string.label_detail_type),
                value = device.deviceType.label,
            )

            InfoDivider()

            // Bond State
            InfoRow(
                label = stringResource(R.string.label_detail_bond),
                value = device.bondState.displayLabel(),
            )

            // Tx Power (있을 때만)
            device.txPower?.let { power ->
                InfoDivider()
                InfoRow(
                    label = stringResource(R.string.label_adv_tx_power),
                    value = "$power dBm",
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String? = null,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = OnSurfaceDim,
            fontSize = 13.sp,
        )
        if (trailing != null) {
            trailing()
        } else if (value != null) {
            Text(
                text = value,
                color = OnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun InfoDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Primary.copy(alpha = 0.06f)),
    )
}

@Composable
private fun QualityBadge(rssi: Int, modifier: Modifier = Modifier) {
    val color = rssiQualityColor(rssi)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
    ) {
        Text(
            text = rssiQualityLabel(rssi),
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        )
    }
}

// --- Glass Card ---

@Composable
internal fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Primary.copy(alpha = GLASS_CARD_BG_ALPHA),
        border = BorderStroke(1.dp, Primary.copy(alpha = GLASS_CARD_BORDER_ALPHA)),
    ) {
        content()
    }
}

// --- Action Buttons ---

@Composable
private fun ConnectionActionButton(
    connectionState: BleConnectionState,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isConnecting = connectionState is BleConnectionState.Connecting ||
        connectionState is BleConnectionState.DiscoveringServices
    val isConnected = connectionState is BleConnectionState.Connected

    if (isConnected) {
        OutlinedButton(
            onClick = onDisconnect,
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Error),
            border = BorderStroke(1.5.dp, Error.copy(alpha = 0.4f)),
        ) {
            Icon(
                imageVector = Icons.Default.LinkOff,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.btn_disconnect),
                fontWeight = FontWeight.Bold,
            )
        }
    } else {
        Button(
            onClick = onConnect,
            enabled = !isConnecting,
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary,
                disabledContainerColor = Primary.copy(alpha = 0.3f),
            ),
        ) {
            if (isConnecting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = OnPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (isConnecting) {
                    stringResource(R.string.label_connecting)
                } else {
                    stringResource(R.string.btn_connect)
                },
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

// --- Helpers ---

@Composable
private fun rssiQualityLabel(rssi: Int): String = when (SignalStrength.from(rssi)) {
    SignalStrength.EXCELLENT -> stringResource(R.string.label_signal_excellent)
    SignalStrength.GOOD -> stringResource(R.string.label_signal_good)
    SignalStrength.FAIR -> stringResource(R.string.label_signal_fair)
    SignalStrength.POOR -> stringResource(R.string.label_signal_weak)
}

@Composable
private fun rssiQualityColor(rssi: Int) = when (SignalStrength.from(rssi)) {
    SignalStrength.EXCELLENT, SignalStrength.GOOD -> WifiGreen
    SignalStrength.FAIR -> NfcAmber
    SignalStrength.POOR -> Error
}

@Composable
private fun BleBondState.displayLabel(): String = when (this) {
    BleBondState.BONDED -> stringResource(R.string.label_bonded)
    BleBondState.BONDING -> stringResource(R.string.label_bonding)
    BleBondState.NONE -> stringResource(R.string.label_not_bonded)
}

private data class WriteTarget(
    val serviceUuid: String,
    val charUuid: String,
    val charName: String,
)
