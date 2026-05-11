package com.jsh.wirelessexplorer.feature.wifi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.feature.wifi.R
import com.jsh.wirelessexplorer.core.model.FrequencyBand
import com.jsh.wirelessexplorer.core.model.TrafficSpeed
import com.jsh.wirelessexplorer.core.model.WifiConnectionInfo
import com.jsh.wirelessexplorer.core.model.WifiStandard
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.SurfaceVariant
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.core.model.WifiSecurityType
import java.util.Locale

private const val BYTES_PER_MEGABIT = 125_000.0

@Composable
fun ConnectedNetworkCard(
    connectionInfo: WifiConnectionInfo,
    trafficSpeed: TrafficSpeed = TrafficSpeed(),
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = null,
                    tint = WifiGreen,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = connectionInfo.ssid,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                ConnectedBadge()
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (connectionInfo.wifiStandard != WifiStandard.UNKNOWN) {
                InfoRow(
                    label = stringResource(R.string.label_wifi_standard),
                    value = "${connectionInfo.wifiStandard.generation} (${connectionInfo.wifiStandard.label})",
                )
            }
            InfoRow(label = stringResource(R.string.label_ip), value = connectionInfo.ipAddress)
            InfoRow(label = stringResource(R.string.label_channel), value = connectionInfo.channel.toString())
            InfoRow(
                label = stringResource(R.string.label_band),
                value = FrequencyBand.from(connectionInfo.frequency).label,
            )
            connectionInfo.channelWidthMhz?.let {
                InfoRow(
                    label = stringResource(R.string.label_channel_width),
                    value = "${it} MHz",
                )
            }
            InfoRow(label = stringResource(R.string.label_speed), value = stringResource(R.string.label_speed_mbps_format, connectionInfo.linkSpeed))
            InfoRow(label = stringResource(R.string.label_security), value = connectionInfo.securityType.label)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                SpeedColumn(
                    label = stringResource(R.string.label_download),
                    bytesPerSec = trafficSpeed.downloadBytesPerSec,
                )
                SpeedColumn(
                    label = stringResource(R.string.label_upload),
                    bytesPerSec = trafficSpeed.uploadBytesPerSec,
                )
            }
        }
    }
}

@Composable
private fun ConnectedBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = WifiGreen.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = stringResource(R.string.label_connected),
            color = WifiGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = OnSurfaceDim,
            fontSize = 14.sp,
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SpeedColumn(
    label: String,
    bytesPerSec: Long,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            color = OnSurfaceDim,
            fontSize = 11.sp,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = formatSpeed(bytesPerSec),
            color = WifiGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun formatSpeed(bytesPerSec: Long): String {
    val mbps = bytesPerSec / BYTES_PER_MEGABIT
    return String.format(Locale.US, "%.1f Mbps", mbps)
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun ConnectedNetworkCardPreview() {
    WirelessExplorerTheme {
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
    }
}
