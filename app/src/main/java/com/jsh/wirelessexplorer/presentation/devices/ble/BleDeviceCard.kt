package com.jsh.wirelessexplorer.presentation.devices.ble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.BleBondState
import com.jsh.wirelessexplorer.domain.model.BleDevice
import com.jsh.wirelessexplorer.domain.model.BleDeviceType
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.OnPrimary
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BleDeviceCard(
    device: BleDevice,
    isConnecting: Boolean,
    onConnectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 신호 강도 바
            SignalBars(rssi = device.rssi)

            Spacer(modifier = Modifier.width(12.dp))

            // 디바이스 정보
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = device.name,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )

                    if (device.bondState == BleBondState.BONDED) {
                        Spacer(modifier = Modifier.width(8.dp))
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = stringResource(R.string.label_bonded),
                                    fontSize = 10.sp,
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = WifiGreen.copy(alpha = 0.15f),
                                labelColor = WifiGreen,
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(24.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = device.address,
                    color = OnSurfaceDim,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 디바이스 타입 뱃지
                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = device.deviceType.label,
                                fontSize = 10.sp,
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = SurfaceVariant,
                            labelColor = OnSurfaceMedium,
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(24.dp),
                    )

                    Text(
                        text = "${device.rssi} dBm",
                        color = OnSurfaceDim,
                        fontSize = 11.sp,
                    )
                }
            }

            // 연결 버튼
            Button(
                onClick = onConnectClick,
                enabled = !isConnecting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary,
                    disabledContainerColor = BleBlue.copy(alpha = 0.3f),
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (isConnecting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = OnPrimary,
                    )
                } else {
                    Text(text = stringResource(R.string.btn_connect))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun BleDeviceCardPreview() {
    WirelessExplorerTheme {
        BleDeviceCard(
            device = BleDevice(
                name = "Galaxy Watch 5",
                address = "AA:BB:CC:DD:EE:01",
                rssi = -45,
                deviceType = BleDeviceType.WEARABLE,
                bondState = BleBondState.BONDED,
                lastSeen = System.currentTimeMillis(),
            ),
            isConnecting = false,
            onConnectClick = {},
        )
    }
}
