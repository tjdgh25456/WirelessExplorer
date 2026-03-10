package com.jsh.wirelessexplorer.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.DeviceHistory
import com.jsh.wirelessexplorer.domain.model.TechType
import com.jsh.wirelessexplorer.presentation.common.SignalBarsIndicator
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.UwbViolet
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.presentation.util.RelativeTimeFormatter

@Composable
fun HistoryDeviceCard(
    device: DeviceHistory,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (icon, tint) = device.techType.iconAndColor()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 기술 타입 아이콘
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = device.techType.label,
                tint = tint,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 디바이스 정보
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = device.deviceName,
                color = OnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            Text(
                text = device.deviceAddress,
                color = OnSurfaceDim,
                fontSize = 12.sp,
                maxLines = 1,
            )
            Text(
                text = RelativeTimeFormatter.formatRelativeTime(device.lastConnectedAt),
                color = OnSurfaceDim,
                fontSize = 11.sp,
            )
        }

        // 신호 강도 바
        if (device.rssi != null) {
            SignalBarsIndicator(rssi = device.rssi)
            Spacer(modifier = Modifier.width(4.dp))
        }

        // 즐겨찾기 토글
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (device.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = stringResource(if (device.isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite),
                tint = if (device.isFavorite) Color(0xFFFBBF24) else OnSurfaceDim,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

private fun TechType.iconAndColor(): Pair<ImageVector, Color> = when (this) {
    TechType.BLE -> Icons.Filled.Bluetooth to BleBlue
    TechType.WIFI -> Icons.Filled.Wifi to WifiGreen
    TechType.NFC -> Icons.Filled.Nfc to NfcAmber
    TechType.UWB -> Icons.Outlined.Sensors to UwbViolet
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun HistoryDeviceCardPreview() {
    WirelessExplorerTheme {
        HistoryDeviceCard(
            device = DeviceHistory(
                id = 1,
                deviceName = "Galaxy Watch 5",
                deviceAddress = "AA:BB:CC:DD:EE:01",
                techType = TechType.BLE,
                rssi = -45,
                isFavorite = true,
                connectCount = 12,
            ),
            onClick = {},
            onToggleFavorite = {},
        )
    }
}
