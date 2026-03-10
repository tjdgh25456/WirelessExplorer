package com.jsh.wirelessexplorer.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.SignalStrength
import com.jsh.wirelessexplorer.domain.model.WifiSecurityType
import com.jsh.wirelessexplorer.domain.model.WifiStatus
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private const val FREQUENCY_DIVISOR = 1000.0

@Composable
fun ConnectionStatusCard(
    wifiStatus: WifiStatus,
    modifier: Modifier = Modifier,
) {
    val signalStrength = SignalStrength.from(wifiStatus.rssi)
    val qualityText = when (signalStrength) {
        SignalStrength.EXCELLENT -> stringResource(R.string.label_signal_excellent)
        SignalStrength.GOOD -> stringResource(R.string.label_signal_good)
        SignalStrength.FAIR -> stringResource(R.string.label_signal_fair)
        SignalStrength.POOR -> stringResource(R.string.label_signal_poor)
    }

    // GHz 대역 표시용 주파수 변환
    val frequencyGhz = String.format("%.1f", wifiStatus.frequency / FREQUENCY_DIVISOR)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = com.jsh.wirelessexplorer.presentation.theme.Surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp),
        ) {
            // 연결 상태 레이블
            Text(
                text = stringResource(R.string.label_connected_to),
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceDim,
                letterSpacing = 1.5.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // SSID
            Text(
                text = wifiStatus.ssid,
                style = MaterialTheme.typography.headlineMedium,
                color = OnSurface,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 보안 타입 배지
            SecurityBadge(securityType = wifiStatus.securityType)

            Spacer(modifier = Modifier.height(20.dp))

            // RSSI 게이지
            RssiGauge(rssi = wifiStatus.rssi)

            Spacer(modifier = Modifier.height(12.dp))

            // 신호 품질 텍스트
            Text(
                text = stringResource(R.string.label_signal_format, qualityText),
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 채널 및 주파수 정보
            Text(
                text = stringResource(R.string.label_channel_format, wifiStatus.channel, frequencyGhz),
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceDim,
            )
        }
    }
}

@Composable
private fun SecurityBadge(
    securityType: WifiSecurityType,
    modifier: Modifier = Modifier,
) {
    val isOpen = securityType == WifiSecurityType.OPEN
    val label = if (isOpen) {
        stringResource(R.string.label_open_network)
    } else {
        stringResource(R.string.label_secured_format, securityType.label)
    }
    val badgeColor = if (isOpen) SurfaceVariant else WifiGreen.copy(alpha = 0.15f)
    val textColor = if (isOpen) OnSurfaceDim else WifiGreen

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = badgeColor,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun ConnectionStatusCardPreview() {
    WirelessExplorerTheme {
        ConnectionStatusCard(
            wifiStatus = WifiStatus(
                ssid = "MyHome_5G",
                rssi = -45,
                linkSpeed = 866,
                frequency = 5200,
                ipAddress = "192.168.1.100",
                securityType = WifiSecurityType.WPA2,
                channel = 40,
                isConnected = true,
            ),
        )
    }
}
