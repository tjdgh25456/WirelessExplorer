package com.jsh.wirelessexplorer.feature.analyze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.jsh.wirelessexplorer.feature.analyze.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.model.TopDevice
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.NfcAmber
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.designsystem.theme.SurfaceVariant
import com.jsh.wirelessexplorer.core.designsystem.theme.UwbViolet
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen

@Composable
fun TopDevicesSection(
    devices: List<TopDevice>,
    modifier: Modifier = Modifier,
) {
    val maxCount = remember(devices) { devices.maxOfOrNull { it.connectCount } ?: 1 }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = stringResource(R.string.label_top_devices),
            color = OnSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        if (devices.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_no_device_data),
                color = OnSurfaceDim,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            return
        }

        devices.forEachIndexed { index, device ->
            key(device.deviceName, device.techType) {
                DeviceRankRow(
                    rank = index + 1,
                    device = device,
                    progress = device.connectCount.toFloat() / maxCount.coerceAtLeast(1),
                )
            }
        }
    }
}

@Composable
private fun DeviceRankRow(
    rank: Int,
    device: TopDevice,
    progress: Float,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 순위
        Text(
            text = "$rank",
            color = if (rank <= 3) Primary else OnSurfaceDim,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(24.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = device.deviceName,
                    color = OnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = device.techType.label,
                    color = device.techType.barColor(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 프로그레스 바
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SurfaceVariant),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(device.techType.barColor()),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${device.connectCount}",
            color = OnSurfaceDim,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(32.dp),
        )
    }
}

private fun TechType.barColor(): Color = when (this) {
    TechType.BLE -> BleBlue
    TechType.WIFI -> WifiGreen
    TechType.NFC -> NfcAmber
    TechType.UWB -> UwbViolet
}
