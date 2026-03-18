package com.jsh.wirelessexplorer.feature.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.Error
import com.jsh.wirelessexplorer.core.designsystem.theme.NfcAmber
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface as ThemeSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.model.BleConnectedDevice
import com.jsh.wirelessexplorer.core.model.SignalStrength
import com.jsh.wirelessexplorer.feature.dashboard.R

@Composable
fun BleConnectedCard(
    devices: List<BleConnectedDevice>,
    onDeviceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ThemeSurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 섹션 헤더
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BleBlue.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.BluetoothConnected,
                            contentDescription = null,
                            tint = BleBlue,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.label_ble_connected, devices.size),
                    color = OnSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 디바이스 카드들
            devices.forEachIndexed { index, device ->
                if (index > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(1.dp)
                            .background(Primary.copy(alpha = 0.06f)),
                    )
                }
                BleConnectedDeviceRow(
                    device = device,
                    onClick = { onDeviceClick(device.address) },
                )
            }
        }
    }
}

@Composable
private fun BleConnectedDeviceRow(
    device: BleConnectedDevice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // BT 아바타 + 연결 뱃지
        Box {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Primary.copy(alpha = 0.1f),
                border = BorderStroke(2.dp, BleBlue.copy(alpha = 0.3f)),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Bluetooth,
                        contentDescription = null,
                        tint = BleBlue,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            // 연결 상태 녹색 점
            Surface(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                color = WifiGreen,
                border = BorderStroke(
                    2.dp,
                    ThemeSurface,
                ),
            ) {}
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 디바이스 정보
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = device.name ?: stringResource(R.string.label_unknown_device),
                color = OnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = device.address,
                color = OnSurfaceDim,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(modifier = Modifier.height(4.dp))

            // RSSI + 서비스 수
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // RSSI 뱃지
                if (device.rssi != 0) {
                    val rssiColor = when (SignalStrength.from(device.rssi)) {
                        SignalStrength.EXCELLENT, SignalStrength.GOOD -> WifiGreen
                        SignalStrength.FAIR -> NfcAmber
                        SignalStrength.POOR -> Error
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = rssiColor.copy(alpha = 0.12f),
                    ) {
                        Text(
                            text = "${device.rssi} dBm",
                            color = rssiColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }

                // 서비스 수
                if (device.serviceCount > 0) {
                    Text(
                        text = stringResource(R.string.label_services_count, device.serviceCount),
                        color = OnSurfaceMedium,
                        fontSize = 11.sp,
                    )
                }
            }
        }

        // 화살표
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = OnSurfaceDim,
            modifier = Modifier.size(14.dp),
        )
    }
}
