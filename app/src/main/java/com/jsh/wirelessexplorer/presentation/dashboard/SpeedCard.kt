package com.jsh.wirelessexplorer.presentation.dashboard

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
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private const val DOWNLOAD_SPEED_DIVISOR = 2
private const val UPLOAD_SPEED_DIVISOR = 4

/**
 * 다운로드/업로드 속도 카드.
 * linkSpeed 기반 추정치를 표시한다.
 */
@Composable
fun SpeedCard(
    label: String,
    icon: ImageVector,
    speedMbps: Int,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = com.jsh.wirelessexplorer.presentation.theme.Surface,
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
                    text = "$speedMbps",
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

/** linkSpeed를 기반으로 다운로드/업로드 추정 속도 카드를 한 쌍으로 표시한다. */
@Composable
fun SpeedCardRow(
    linkSpeed: Int,
    modifier: Modifier = Modifier,
) {
    val downloadEstimate = linkSpeed / DOWNLOAD_SPEED_DIVISOR
    val uploadEstimate = linkSpeed / UPLOAD_SPEED_DIVISOR

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        SpeedCard(
            label = stringResource(R.string.label_download),
            icon = Icons.Rounded.ArrowDownward,
            speedMbps = downloadEstimate,
            accentColor = WifiGreen,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(12.dp))
        SpeedCard(
            label = stringResource(R.string.label_upload),
            icon = Icons.Rounded.ArrowUpward,
            speedMbps = uploadEstimate,
            accentColor = com.jsh.wirelessexplorer.presentation.theme.Primary,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun SpeedCardRowPreview() {
    WirelessExplorerTheme {
        SpeedCardRow(linkSpeed = 866)
    }
}
