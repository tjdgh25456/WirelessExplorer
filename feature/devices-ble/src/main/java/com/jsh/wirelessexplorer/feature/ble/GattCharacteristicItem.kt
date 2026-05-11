package com.jsh.wirelessexplorer.feature.ble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.feature.ble.R
import com.jsh.wirelessexplorer.core.model.GattCharacteristicInfo
import com.jsh.wirelessexplorer.core.model.GattProperty
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.SurfaceVariant
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen
import com.jsh.wirelessexplorer.core.designsystem.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private val PropertyColors = mapOf(
    GattProperty.READ to BleBlue,
    GattProperty.WRITE to Color(0xFFF59E0B),
    GattProperty.WRITE_NO_RESPONSE to Color(0xFFF59E0B),
    GattProperty.NOTIFY to WifiGreen,
    GattProperty.INDICATE to Color(0xFF8B5CF6),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GattCharacteristicItem(
    characteristic: GattCharacteristicInfo,
    onReadClick: () -> Unit,
    onWriteClick: () -> Unit,
    onNotifyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        // Characteristic 이름
        Text(
            text = characteristic.name,
            color = OnSurface,
            fontSize = 13.sp,
        )

        // UUID
        Text(
            text = characteristic.uuid,
            color = OnSurfaceDim,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Property 뱃지
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            characteristic.properties.forEach { property ->
                PropertyBadge(
                    label = property.name,
                    color = PropertyColors[property] ?: OnSurfaceMedium,
                )
            }
        }

        // 현재 값 표시
        characteristic.value?.let { bytes ->
            Spacer(modifier = Modifier.height(6.dp))

            val hexString = bytes.joinToString(" ") { "%02X".format(it) }
            val decodedString = runCatching { String(bytes, Charsets.UTF_8) }.getOrDefault("")
                .filter { it.isLetterOrDigit() || it.isWhitespace() || it == '.' || it == '-' }

            Text(
                text = stringResource(R.string.label_hex_format, hexString),
                color = BleBlue,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
            )
            if (decodedString.isNotBlank()) {
                Text(
                    text = stringResource(R.string.label_utf8_format, decodedString),
                    color = OnSurfaceMedium,
                    fontSize = 11.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Descriptors
        if (characteristic.descriptors.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            characteristic.descriptors.forEach { descriptor ->
                Text(
                    text = "${descriptor.name} (${descriptor.uuid.take(8)})",
                    color = OnSurfaceDim,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 액션 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (GattProperty.READ in characteristic.properties) {
                IconButton(
                    onClick = onReadClick,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = BleBlue,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = stringResource(R.string.cd_read),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            val hasWrite = GattProperty.WRITE in characteristic.properties ||
                GattProperty.WRITE_NO_RESPONSE in characteristic.properties
            if (hasWrite) {
                IconButton(
                    onClick = onWriteClick,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFFF59E0B),
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.cd_write),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            val hasNotify = GattProperty.NOTIFY in characteristic.properties ||
                GattProperty.INDICATE in characteristic.properties
            if (hasNotify) {
                IconButton(
                    onClick = onNotifyClick,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = WifiGreen,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(R.string.cd_subscribe),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyBadge(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = label,
                color = color,
                fontSize = 9.sp,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun GattCharacteristicItemPreview() {
    WirelessExplorerTheme {
        GattCharacteristicItem(
            characteristic = GattCharacteristicInfo(
                uuid = "00002a29-0000-1000-8000-00805f9b34fb",
                serviceUuid = "0000180a-0000-1000-8000-00805f9b34fb",
                name = "Manufacturer Name",
                properties = setOf(GattProperty.READ, GattProperty.WRITE, GattProperty.NOTIFY),
                value = byteArrayOf(0x48, 0x65, 0x6C, 0x6C, 0x6F),
            ),
            onReadClick = {},
            onWriteClick = {},
            onNotifyClick = {},
        )
    }
}
