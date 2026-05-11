package com.jsh.wirelessexplorer.feature.ble.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.core.model.GattServiceInfo
import com.jsh.wirelessexplorer.feature.ble.GattServiceCard
import com.jsh.wirelessexplorer.feature.ble.R

@Composable
internal fun GattServicesSection(
    services: List<GattServiceInfo>,
    onReadCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    onWriteCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    onNotifyCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_gatt_services_format, services.size).uppercase(),
            color = OnSurfaceMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(start = 4.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            services.forEach { service ->
                GattServiceCard(
                    service = service,
                    onReadCharacteristic = onReadCharacteristic,
                    onWriteCharacteristic = onWriteCharacteristic,
                    onNotifyCharacteristic = onNotifyCharacteristic,
                )
            }
        }
    }
}
