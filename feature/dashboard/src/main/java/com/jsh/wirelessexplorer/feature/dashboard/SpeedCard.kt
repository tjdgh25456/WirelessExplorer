package com.jsh.wirelessexplorer.feature.dashboard

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
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.feature.dashboard.R
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private const val BYTES_PER_MEGABIT = 125_000.0

@Composable
fun SpeedCard(
    label: String,
    icon: ImageVector,
    speedMbps: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = com.jsh.wirelessexplorer.core.designsystem.theme.Surface,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceDim,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = speedMbps,
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.label_mbps),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceDim,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}

@Composable
fun SpeedCardRow(
    downloadBytesPerSec: Long,
    uploadBytesPerSec: Long,
    modifier: Modifier = Modifier,
) {
    val downloadMbps = formatSpeed(downloadBytesPerSec)
    val uploadMbps = formatSpeed(uploadBytesPerSec)

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        SpeedCard(
            label = stringResource(R.string.label_download),
            icon = Icons.Rounded.ArrowDownward,
            speedMbps = downloadMbps,
            accentColor = WifiGreen,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(12.dp))
        SpeedCard(
            label = stringResource(R.string.label_upload),
            icon = Icons.Rounded.ArrowUpward,
            speedMbps = uploadMbps,
            accentColor = com.jsh.wirelessexplorer.core.designsystem.theme.Primary,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun formatSpeed(bytesPerSec: Long): String {
    val mbps = bytesPerSec / BYTES_PER_MEGABIT
    return String.format(java.util.Locale.US, "%.1f", mbps)
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun SpeedCardRowPreview() {
    WirelessExplorerTheme {
        SpeedCardRow(downloadBytesPerSec = 5_000_000, uploadBytesPerSec = 1_500_000)
    }
}
