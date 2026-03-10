package com.jsh.wirelessexplorer.presentation.devices.wifi

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
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.NetworkWifi1Bar
import androidx.compose.material.icons.filled.NetworkWifi2Bar
import androidx.compose.material.icons.filled.NetworkWifi3Bar
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.domain.model.FrequencyBand
import com.jsh.wirelessexplorer.domain.model.SignalStrength
import com.jsh.wirelessexplorer.domain.model.WifiNetwork
import com.jsh.wirelessexplorer.domain.model.WifiSecurityType
import com.jsh.wirelessexplorer.domain.model.WifiStandard
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WifiNetworkCard(
    network: WifiNetwork,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SignalIcon(rssi = network.rssi)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = network.ssid,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    WifiStandardBadge(wifiStandard = network.wifiStandard)
                    FrequencyBadge(frequency = network.frequency)
                    network.channelWidthMhz?.let { ChannelWidthBadge(widthMhz = it) }
                    SecurityPill(securityType = network.securityType)
                }
            }

            Text(
                text = "${network.rssi} dBm",
                color = OnSurfaceDim,
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun SignalIcon(rssi: Int, modifier: Modifier = Modifier) {
    val strength = SignalStrength.from(rssi)
    val icon: ImageVector = when (strength) {
        SignalStrength.EXCELLENT -> Icons.Default.NetworkWifi
        SignalStrength.GOOD -> Icons.Default.NetworkWifi3Bar
        SignalStrength.FAIR -> Icons.Default.NetworkWifi2Bar
        SignalStrength.POOR -> Icons.Default.NetworkWifi1Bar
    }
    val tint = when (strength) {
        SignalStrength.EXCELLENT -> WifiGreen
        SignalStrength.GOOD -> WifiGreen.copy(alpha = 0.8f)
        SignalStrength.FAIR -> Color(0xFFF59E0B)
        SignalStrength.POOR -> Color(0xFFEF4444)
    }

    Icon(
        imageVector = icon,
        contentDescription = "${strength.name} signal",
        tint = tint,
        modifier = modifier.size(24.dp),
    )
}

@Composable
private fun WifiStandardBadge(wifiStandard: WifiStandard, modifier: Modifier = Modifier) {
    if (wifiStandard == WifiStandard.UNKNOWN) return

    val badgeColor = when (wifiStandard) {
        WifiStandard.WIFI_7 -> Color(0xFF8B5CF6)  // Purple
        WifiStandard.WIFI_6E -> Color(0xFF06B6D4) // Cyan
        WifiStandard.WIFI_6 -> Color(0xFF3B82F6)  // Blue
        WifiStandard.WIFI_5 -> Color(0xFF22C55E)  // Green
        else -> OnSurfaceDim
    }

    Surface(
        modifier = modifier,
        color = badgeColor.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = wifiStandard.generation,
            color = badgeColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun ChannelWidthBadge(widthMhz: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = OnSurfaceDim.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = "${widthMhz}MHz",
            color = OnSurfaceDim,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun FrequencyBadge(frequency: Int, modifier: Modifier = Modifier) {
    val band = FrequencyBand.from(frequency)
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = band.label,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun SecurityPill(securityType: WifiSecurityType, modifier: Modifier = Modifier) {
    val backgroundColor = when (securityType) {
        WifiSecurityType.WPA3 -> Color(0xFFEF4444)
        WifiSecurityType.WPA2 -> Color(0xFFEAB308)
        WifiSecurityType.WPA -> Color(0xFFF97316)
        WifiSecurityType.WEP -> Color(0xFFF97316)
        WifiSecurityType.OPEN -> Color(0xFF22C55E)
    }

    Surface(
        modifier = modifier,
        color = backgroundColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = securityType.label,
            color = backgroundColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun WifiNetworkCardPreview() {
    WirelessExplorerTheme {
        WifiNetworkCard(
            network = WifiNetwork(
                ssid = "Office_Network",
                bssid = "11:22:33:44:55:66",
                rssi = -55,
                frequency = 5200,
                securityType = WifiSecurityType.WPA3,
                isConnected = false,
            ),
        )
    }
}
