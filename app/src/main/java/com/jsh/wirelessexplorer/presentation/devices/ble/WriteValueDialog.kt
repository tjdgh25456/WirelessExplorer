package com.jsh.wirelessexplorer.presentation.devices.ble

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.Error
import com.jsh.wirelessexplorer.presentation.theme.OnPrimary
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private enum class InputMode { HEX, TEXT }

@Composable
fun WriteValueDialog(
    characteristicName: String,
    onDismiss: () -> Unit,
    onWrite: (ByteArray) -> Unit,
) {
    var inputMode by remember { mutableStateOf(InputMode.HEX) }
    var inputText by remember { mutableStateOf("") }
    var parseError by remember { mutableStateOf<String?>(null) }
    val errorInvalidHex = stringResource(R.string.error_invalid_hex)
    val errorInputEmpty = stringResource(R.string.error_input_empty)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = {
            Text(
                text = stringResource(R.string.title_write_value),
                color = OnSurface,
            )
        },
        text = {
            Column {
                Text(
                    text = characteristicName,
                    color = OnSurfaceMedium,
                    fontSize = 13.sp,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // HEX / Text 모드 전환
                Row {
                    FilterChip(
                        selected = inputMode == InputMode.HEX,
                        onClick = {
                            inputMode = InputMode.HEX
                            inputText = ""
                            parseError = null
                        },
                        label = { Text(stringResource(R.string.label_hex)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BleBlue.copy(alpha = 0.2f),
                            selectedLabelColor = BleBlue,
                            containerColor = SurfaceVariant,
                            labelColor = OnSurfaceMedium,
                        ),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilterChip(
                        selected = inputMode == InputMode.TEXT,
                        onClick = {
                            inputMode = InputMode.TEXT
                            inputText = ""
                            parseError = null
                        },
                        label = { Text(stringResource(R.string.label_text)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BleBlue.copy(alpha = 0.2f),
                            selectedLabelColor = BleBlue,
                            containerColor = SurfaceVariant,
                            labelColor = OnSurfaceMedium,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        parseError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = if (inputMode == InputMode.HEX) stringResource(R.string.hint_hex_input) else stringResource(R.string.hint_text_input),
                            color = OnSurfaceDim,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        focusedBorderColor = BleBlue,
                        unfocusedBorderColor = SurfaceVariant,
                        cursorColor = BleBlue,
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = inputMode == InputMode.HEX,
                    isError = parseError != null,
                    supportingText = parseError?.let {
                        { Text(text = it, color = Error, fontSize = 11.sp) }
                    },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bytes = parseInput(inputText.trim(), inputMode)
                    if (bytes != null) {
                        onWrite(bytes)
                    } else {
                        parseError = if (inputMode == InputMode.HEX) {
                            errorInvalidHex
                        } else {
                            errorInputEmpty
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary,
                ),
            ) {
                Text(stringResource(R.string.btn_write))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel), color = OnSurfaceMedium)
            }
        },
    )
}

/**
 * 입력 모드에 따라 문자열을 ByteArray로 파싱한다.
 */
private fun parseInput(input: String, mode: InputMode): ByteArray? {
    if (input.isBlank()) return null

    return when (mode) {
        InputMode.HEX -> {
            runCatching {
                input.replace("\\s+".toRegex(), "")
                    .also { require(it.length % 2 == 0) { "홀수 길이 HEX" } }
                    .chunked(2)
                    .map { it.toInt(16).toByte() }
                    .toByteArray()
            }.getOrNull()
        }
        InputMode.TEXT -> input.toByteArray(Charsets.UTF_8)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun WriteValueDialogPreview() {
    WirelessExplorerTheme {
        WriteValueDialog(
            characteristicName = "Manufacturer Name",
            onDismiss = {},
            onWrite = {},
        )
    }
}
