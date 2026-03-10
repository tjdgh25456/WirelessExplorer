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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.GattServiceInfo
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.Error
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GattBottomSheet(
    deviceName: String,
    services: List<GattServiceInfo>,
    onDismiss: () -> Unit,
    onDisconnect: () -> Unit,
    onReadCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
    onWriteCharacteristic: (serviceUuid: String, charUuid: String, value: ByteArray) -> Unit,
    onNotifyCharacteristic: (serviceUuid: String, charUuid: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // 쓰기 다이얼로그 상태
    var writeTarget by remember { mutableStateOf<WriteTarget?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Background,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp),
                shape = CircleShape,
                color = OnSurfaceMedium.copy(alpha = 0.4f),
            ) {}
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            // 헤더: 블루투스 아이콘 + 디바이스 이름 + 연결 상태
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bluetooth,
                    contentDescription = null,
                    tint = BleBlue,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = deviceName,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 연결 상태 녹색 점
                Surface(
                    modifier = Modifier.size(8.dp),
                    shape = CircleShape,
                    color = WifiGreen,
                ) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GATT 서비스 수 라벨
            Text(
                text = stringResource(R.string.label_gatt_services_format, services.size),
                color = OnSurfaceMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 서비스 목록
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = services,
                    key = { it.uuid },
                ) { service ->
                    GattServiceCard(
                        service = service,
                        onReadCharacteristic = onReadCharacteristic,
                        onWriteCharacteristic = { sUuid, cUuid ->
                            val char = service.characteristics.find { it.uuid == cUuid }
                            writeTarget = WriteTarget(sUuid, cUuid, char?.name ?: cUuid)
                        },
                        onNotifyCharacteristic = onNotifyCharacteristic,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 연결 해제 버튼
            OutlinedButton(
                onClick = onDisconnect,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Error),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Error.copy(alpha = 0.5f)),
                ),
            ) {
                Text(stringResource(R.string.btn_disconnect))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 쓰기 값 입력 다이얼로그
    writeTarget?.let { target ->
        WriteValueDialog(
            characteristicName = target.charName,
            onDismiss = { writeTarget = null },
            onWrite = { value ->
                onWriteCharacteristic(target.serviceUuid, target.charUuid, value)
                writeTarget = null
            },
        )
    }
}

private data class WriteTarget(
    val serviceUuid: String,
    val charUuid: String,
    val charName: String,
)
