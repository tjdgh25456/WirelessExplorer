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
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.SignalCellularAlt
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
import com.jsh.wirelessexplorer.domain.model.DashboardStats
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen

@Composable
fun StatsRow(
    stats: DashboardStats,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        StatMiniCard(
            icon = Icons.Rounded.Devices,
            value = "${stats.deviceCount}",
            label = stringResource(R.string.label_devices_scanned),
            accentColor = BleBlue,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        StatMiniCard(
            icon = Icons.Rounded.Link,
            value = "${stats.totalConnections}",
            label = stringResource(R.string.label_total_connections),
            accentColor = WifiGreen,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        StatMiniCard(
            icon = Icons.Rounded.SignalCellularAlt,
            value = stats.bestSignal?.let { stringResource(R.string.label_dbm_format, it) }
                ?: stringResource(R.string.label_dbm_placeholder),
            label = stringResource(R.string.label_best_signal),
            accentColor = NfcAmber,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatMiniCard(
    icon: ImageVector,
    value: String,
    label: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = com.jsh.wirelessexplorer.presentation.theme.Surface,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(20.dp),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = OnSurface,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceDim,
                maxLines = 1,
            )
        }
    }
}
