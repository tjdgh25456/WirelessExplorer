package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary
import com.jsh.wirelessexplorer.core.model.BleDataStatus
import com.jsh.wirelessexplorer.core.model.BleDevice
import com.jsh.wirelessexplorer.core.model.BleManufacturerIds
import com.jsh.wirelessexplorer.core.model.BlePhy
import com.jsh.wirelessexplorer.feature.ble.R

private const val HEX_BYTES_PER_LINE = 16

@Composable
internal fun AdvertisingDataSection(
    device: BleDevice,
    modifier: Modifier = Modifier,
) {
    val adv = device.advertisingData ?: return

    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(text = stringResource(R.string.section_advertising_data))

        Spacer(modifier = Modifier.height(8.dp))

        GlassCard {
            Column(modifier = Modifier.padding(16.dp)) {
                var hasContent = false

                // Flags
                adv.flags?.let { flags ->
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_flags),
                        value = formatAdvFlags(flags),
                        helpText = stringResource(R.string.help_adv_flags),
                    )
                    hasContent = true
                }

                // Local Name
                adv.localName?.let { name ->
                    if (hasContent) AdvDivider()
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_local_name),
                        value = name,
                    )
                    hasContent = true
                }

                // PHY
                if (adv.primaryPhy != BlePhy.UNKNOWN || adv.secondaryPhy != BlePhy.UNKNOWN) {
                    if (hasContent) AdvDivider()
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_phy),
                        value = "${adv.primaryPhy.displayName()} / ${adv.secondaryPhy.displayName()}",
                        helpText = stringResource(R.string.help_adv_phy),
                    )
                    hasContent = true
                }

                // Advertising Set ID
                adv.advertisingSid?.let { sid ->
                    if (hasContent) AdvDivider()
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_sid),
                        value = sid.toString(),
                        helpText = stringResource(R.string.help_adv_sid),
                    )
                    hasContent = true
                }

                // Data Status
                if (adv.dataStatus != BleDataStatus.UNKNOWN) {
                    if (hasContent) AdvDivider()
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_data_status),
                        value = adv.dataStatus.displayName(),
                        helpText = stringResource(R.string.help_adv_data_status),
                    )
                    hasContent = true
                }

                // Tx Power
                device.txPower?.let { power ->
                    if (hasContent) AdvDivider()
                    AdvertisingInfoItem(
                        label = stringResource(R.string.label_adv_tx_power),
                        value = "$power dBm",
                        helpText = stringResource(R.string.help_adv_tx_power),
                    )
                }
            }
        }

        // Service UUIDs
        if (device.serviceUuids.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SubSectionHeader(
                text = stringResource(R.string.label_adv_service_uuids, device.serviceUuids.size),
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    device.serviceUuids.forEachIndexed { index, uuid ->
                        if (index > 0) AdvDivider()
                        Text(
                            text = uuid,
                            color = BleBlue,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }
            }
        }

        // Service Data
        if (adv.serviceData.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SubSectionHeader(text = stringResource(R.string.label_adv_service_data))
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    adv.serviceData.entries.forEachIndexed { index, (uuid, bytes) ->
                        if (index > 0) AdvDivider()
                        HexDataRow(label = uuid, data = bytes)
                    }
                }
            }
        }

        // Manufacturer Data
        if (device.manufacturerData.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            SubSectionHeader(text = stringResource(R.string.label_adv_manufacturer_data))
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard {
                Column(modifier = Modifier.padding(12.dp)) {
                    device.manufacturerData.entries.forEachIndexed { index, (companyId, bytes) ->
                        if (index > 0) AdvDivider()
                        val companyName = BleManufacturerIds.names[companyId]
                        val label = if (companyName != null) {
                            "$companyName (0x${"%04X".format(companyId)})"
                        } else {
                            "0x${"%04X".format(companyId)}"
                        }
                        HexDataRow(
                            label = label,
                            data = bytes,
                            helpText = stringResource(R.string.help_adv_manufacturer),
                        )
                    }
                }
            }
        }

        // Raw Advertisement Data
        adv.rawBytes?.let { raw ->
            if (raw.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                RawBytesSection(rawBytes = raw)
            }
        }
    }
}

@Composable
private fun HexDataRow(
    label: String,
    data: ByteArray,
    helpText: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        AdvertisingInfoItem(
            label = label,
            value = data.toHexString(),
            isMonospace = true,
            helpText = helpText,
        )
    }
}

@Composable
private fun RawBytesSection(
    rawBytes: ByteArray,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    GlassCard(modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.label_adv_raw_data, rawBytes.size),
                    color = OnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = OnSurfaceDim,
                    modifier = Modifier.size(18.dp),
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Primary.copy(alpha = 0.03f),
                ) {
                    Text(
                        text = rawBytes.toFormattedHexDump(),
                        color = OnSurfaceDim,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}

// --- Section Headers ---

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        color = OnSurfaceMedium,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = modifier.padding(start = 4.dp),
    )
}

@Composable
private fun SubSectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = OnSurfaceDim,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(start = 4.dp),
    )
}

@Composable
private fun AdvDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(1.dp)
            .background(Primary.copy(alpha = 0.06f)),
    )
}

// --- Formatters ---

private fun ByteArray.toHexString(): String =
    joinToString(" ") { "%02X".format(it) }

private fun ByteArray.toFormattedHexDump(): String = buildString {
    for (i in this@toFormattedHexDump.indices step HEX_BYTES_PER_LINE) {
        val end = minOf(i + HEX_BYTES_PER_LINE, this@toFormattedHexDump.size)
        val chunk = this@toFormattedHexDump.sliceArray(i until end)
        append("%04X  ".format(i))
        append(chunk.joinToString(" ") { "%02X".format(it) })
        appendLine()
    }
}

@Composable
private fun formatAdvFlags(flags: Int): String {
    val parts = mutableListOf<String>()
    if (flags and 0x01 != 0) parts += stringResource(R.string.label_flag_le_limited)
    if (flags and 0x02 != 0) parts += stringResource(R.string.label_flag_le_general)
    if (flags and 0x04 != 0) parts += stringResource(R.string.label_flag_bredr_not_supported)
    if (flags and 0x08 != 0) parts += stringResource(R.string.label_flag_le_bredr_controller)
    if (flags and 0x10 != 0) parts += stringResource(R.string.label_flag_le_bredr_host)
    val hex = "0x%02X".format(flags)
    return if (parts.isEmpty()) {
        hex
    } else {
        "$hex — ${parts.joinToString(", ")}"
    }
}

@Composable
private fun BlePhy.displayName(): String = when (this) {
    BlePhy.PHY_1M -> stringResource(R.string.label_phy_1m)
    BlePhy.PHY_2M -> stringResource(R.string.label_phy_2m)
    BlePhy.PHY_CODED -> stringResource(R.string.label_phy_coded)
    BlePhy.UNKNOWN -> stringResource(R.string.label_phy_unknown)
}

@Composable
private fun BleDataStatus.displayName(): String = when (this) {
    BleDataStatus.COMPLETE -> stringResource(R.string.label_data_status_complete)
    BleDataStatus.INCOMPLETE -> stringResource(R.string.label_data_status_incomplete)
    BleDataStatus.TRUNCATED -> stringResource(R.string.label_data_status_truncated)
    BleDataStatus.UNKNOWN -> stringResource(R.string.label_data_status_unknown)
}
