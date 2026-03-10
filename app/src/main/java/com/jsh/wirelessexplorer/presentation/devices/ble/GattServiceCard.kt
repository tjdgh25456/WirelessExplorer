package com.jsh.wirelessexplorer.presentation.devices.ble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.GattServiceInfo
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.domain.model.GattCharacteristicInfo
import com.jsh.wirelessexplorer.domain.model.GattProperty

@Composable
fun GattServiceCard(
    service: GattServiceInfo,
    onReadCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    onWriteCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    onNotifyCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
    ) {
        Column {
            // 서비스 헤더 (클릭하여 펼침/접기)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.name,
                        color = OnSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = service.uuid,
                        color = OnSurfaceDim,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.label_characteristics_format, service.characteristics.size),
                        color = OnSurfaceMedium,
                        fontSize = 11.sp,
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) stringResource(R.string.cd_collapse) else stringResource(R.string.cd_expand),
                    tint = BleBlue,
                )
            }

            // Characteristic 목록
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column {
                    service.characteristics.forEachIndexed { index, characteristic ->
                        if (index > 0) {
                            HorizontalDivider(
                                color = OnSurfaceDim.copy(alpha = 0.15f),
                                thickness = 0.5.dp,
                            )
                        }
                        GattCharacteristicItem(
                            characteristic = characteristic,
                            onReadClick = {
                                onReadCharacteristic(service.uuid, characteristic.uuid)
                            },
                            onWriteClick = {
                                onWriteCharacteristic(service.uuid, characteristic.uuid)
                            },
                            onNotifyClick = {
                                onNotifyCharacteristic(service.uuid, characteristic.uuid)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun GattServiceCardPreview() {
    WirelessExplorerTheme {
        GattServiceCard(
            service = GattServiceInfo(
                uuid = "0000180a-0000-1000-8000-00805f9b34fb",
                name = "Device Information",
                characteristics = listOf(
                    GattCharacteristicInfo(
                        uuid = "00002a29-0000-1000-8000-00805f9b34fb",
                        serviceUuid = "0000180a-0000-1000-8000-00805f9b34fb",
                        name = "Manufacturer Name",
                        properties = setOf(GattProperty.READ),
                        value = null,
                    ),
                    GattCharacteristicInfo(
                        uuid = "00002a24-0000-1000-8000-00805f9b34fb",
                        serviceUuid = "0000180a-0000-1000-8000-00805f9b34fb",
                        name = "Model Number",
                        properties = setOf(GattProperty.READ, GattProperty.NOTIFY),
                        value = null,
                    ),
                ),
            ),
            onReadCharacteristic = { _, _ -> },
            onWriteCharacteristic = { _, _ -> },
            onNotifyCharacteristic = { _, _ -> },
        )
    }
}
