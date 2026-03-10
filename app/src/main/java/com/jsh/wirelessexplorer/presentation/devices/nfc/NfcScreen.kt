package com.jsh.wirelessexplorer.presentation.devices.nfc

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.NdefRecord
import com.jsh.wirelessexplorer.domain.model.NfcState
import com.jsh.wirelessexplorer.domain.model.NfcTagData
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme

private const val PULSE_DURATION_MS = 1200
private const val CHECK_COLOR_DURATION_MS = 400

@Composable
fun NfcScreen(
    viewModel: NfcViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as Activity
    val clipboardManager = LocalClipboardManager.current

    // Reader 모드 생명주기 관리
    DisposableEffect(Unit) {
        viewModel.enableReader(activity)
        onDispose { viewModel.disableReader(activity) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (val state = uiState.state) {
            is NfcState.Waiting -> WaitingContent()

            is NfcState.TagDetected -> TagDetectedContent(
                state = state,
                onScanAgain = { viewModel.resetToWaiting() },
                onCopyAll = { tag ->
                    val text = buildTagSummary(tag)
                    clipboardManager.setText(AnnotatedString(text))
                },
            )

            is NfcState.NotSupported -> StatusContent(
                icon = Icons.Default.ErrorOutline,
                iconColor = Color.Gray,
                title = stringResource(R.string.title_nfc_not_supported),
                description = stringResource(R.string.desc_nfc_not_supported),
            )

            is NfcState.Disabled -> {
                StatusContent(
                    icon = Icons.Default.Nfc,
                    iconColor = NfcAmber.copy(alpha = 0.5f),
                    title = stringResource(R.string.title_nfc_disabled),
                    description = stringResource(R.string.desc_nfc_disabled),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // NFC 설정 화면으로 이동하는 버튼
                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NfcAmber),
                ) {
                    Icon(
                        imageVector = Icons.Default.Nfc,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.btn_open_nfc_settings), color = Color.Black)
                }
            }

            is NfcState.Error -> StatusContent(
                icon = Icons.Default.ErrorOutline,
                iconColor = MaterialTheme.colorScheme.error,
                title = stringResource(R.string.title_error),
                description = state.message,
            )
        }
    }
}

@Composable
private fun WaitingContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "nfc_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = PULSE_DURATION_MS),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse_alpha",
    )
    val glowSize by infiniteTransition.animateFloat(
        initialValue = 80f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = PULSE_DURATION_MS),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow_size",
    )

    Spacer(modifier = Modifier.height(80.dp))

    // NFC 아이콘 + Amber glow
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(glowSize.dp)
                .alpha(pulseAlpha * 0.2f)
                .shadow(
                    elevation = 24.dp,
                    shape = CircleShape,
                    ambientColor = NfcAmber,
                    spotColor = NfcAmber,
                )
                .background(NfcAmber.copy(alpha = 0.1f), CircleShape),
        )
        Icon(
            imageVector = Icons.Default.Nfc,
            contentDescription = stringResource(R.string.label_nfc),
            modifier = Modifier
                .size(64.dp)
                .alpha(pulseAlpha),
            tint = NfcAmber,
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(R.string.label_hold_near_tag),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.desc_position_device),
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(24.dp))

    // "NFC ACTIVE" chip
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF22C55E).copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF22C55E), CircleShape),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.label_nfc_active),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF22C55E),
            )
        }
    }
}

@Composable
private fun TagDetectedContent(
    state: NfcState.TagDetected,
    onScanAgain: () -> Unit,
    onCopyAll: (com.jsh.wirelessexplorer.domain.model.NfcTagData) -> Unit,
) {
    val tagData = state.tag

    val checkColor by animateColorAsState(
        targetValue = Color(0xFF22C55E),
        animationSpec = tween(CHECK_COLOR_DURATION_MS),
        label = "check_color",
    )

    Spacer(modifier = Modifier.height(32.dp))

    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = stringResource(R.string.cd_tag_detected),
        modifier = Modifier.size(48.dp),
        tint = checkColor,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(R.string.label_tag_detected),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(24.dp))

    NfcTagCard(tagData = tagData)

    Spacer(modifier = Modifier.height(24.dp))

    // Action buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            onClick = onScanAgain,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NfcAmber),
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.btn_scan_again))
        }

        Button(
            onClick = { onCopyAll(tagData) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = NfcAmber),
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.btn_copy_all_data), color = Color.Black)
        }
    }
}

@Composable
private fun StatusContent(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    description: String,
) {
    Spacer(modifier = Modifier.height(100.dp))

    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = iconColor,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 32.dp),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun NfcScreenWaitingPreview() {
    WirelessExplorerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WaitingContent()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun NfcScreenTagDetectedPreview() {
    WirelessExplorerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TagDetectedContent(
                state = NfcState.TagDetected(
                    tag = NfcTagData(
                        id = "04:A2:1B:3C:5D:6E:7F",
                        techList = listOf("NfcA", "Ndef", "MifareUltralight"),
                        ndefRecords = listOf(
                            NdefRecord.TextRecord(text = "Hello NFC", language = "en"),
                            NdefRecord.UriRecord(uri = "https://example.com"),
                        ),
                    ),
                ),
                onScanAgain = {},
                onCopyAll = {},
            )
        }
    }
}

/** 태그 데이터를 클립보드 복사용 텍스트로 요약한다. */
private fun buildTagSummary(tag: com.jsh.wirelessexplorer.domain.model.NfcTagData): String = buildString {
    appendLine("NFC Tag Data")
    appendLine("============")
    appendLine("ID: ${tag.id}")
    appendLine("Technologies: ${tag.techList.joinToString(", ")}")

    if (tag.ndefRecords.isNotEmpty()) {
        appendLine()
        appendLine("NDEF Records (${tag.ndefRecords.size}):")
        tag.ndefRecords.forEachIndexed { i, record ->
            append("  [${i + 1}] ")
            when (record) {
                is NdefRecord.TextRecord -> appendLine("TEXT: ${record.text} (${record.language})")
                is NdefRecord.UriRecord -> appendLine("URI: ${record.uri}")
                is NdefRecord.MimeRecord -> appendLine("MIME: ${record.mimeType} (${record.data.size} bytes)")
                is NdefRecord.ExternalRecord -> appendLine("EXTERNAL: ${record.domain}:${record.type} (${record.data.size} bytes)")
                is NdefRecord.UnknownRecord -> appendLine("UNKNOWN: ${record.payload.size} bytes")
            }
        }
    }
}
