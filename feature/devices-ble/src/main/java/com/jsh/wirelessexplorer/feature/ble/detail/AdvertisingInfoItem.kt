package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Surface
import com.jsh.wirelessexplorer.feature.ble.R

@Composable
internal fun AdvertisingInfoItem(
    label: String,
    value: String,
    helpText: String? = null,
    isMonospace: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var showHelp by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = OnSurfaceMedium,
                fontSize = 11.sp,
            )
            Text(
                text = value,
                color = OnSurface,
                fontSize = 13.sp,
                fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            )
        }

        if (helpText != null) {
            IconButton(
                onClick = { showHelp = true },
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.cd_help_info, label),
                    tint = OnSurfaceDim,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }

    if (showHelp && helpText != null) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            containerColor = Surface,
            title = { Text(label, color = OnSurface) },
            text = { Text(helpText, color = OnSurfaceMedium, fontSize = 13.sp) },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) {
                    Text(stringResource(R.string.btn_ok), color = BleBlue)
                }
            },
        )
    }
}
