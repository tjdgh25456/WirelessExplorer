package com.jsh.wirelessexplorer.presentation.devices.nfc

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
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.jsh.wirelessexplorer.R
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.domain.model.NdefRecord
import com.jsh.wirelessexplorer.domain.model.NfcTagData
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NfcTagCard(
    tagData: NfcTagData,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Tag ID
            Text(
                text = stringResource(R.string.label_tag_id),
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceDim,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tagData.id,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                ),
                color = NfcAmber,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tech list
            Text(
                text = stringResource(R.string.label_technologies),
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceDim,
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                tagData.techList.forEach { tech ->
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = tech,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = SurfaceVariant,
                            labelColor = NfcAmber,
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = NfcAmber.copy(alpha = 0.3f),
                        ),
                    )
                }
            }

            // NDEF Records
            if (tagData.ndefRecords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = SurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.label_ndef_records_format, tagData.ndefRecords.size),
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceDim,
                )
                Spacer(modifier = Modifier.height(12.dp))

                tagData.ndefRecords.forEachIndexed { index, record ->
                    if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                    NdefRecordItem(
                        record = record,
                        onUriClick = { uri -> uriHandler.openUri(uri) },
                    )
                }
            }
        }
    }
}

@Composable
private fun NdefRecordItem(
    record: NdefRecord,
    onUriClick: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            when (record) {
                is NdefRecord.TextRecord -> {
                    RecordHeader(icon = { Icon(Icons.Default.TextFields, null, Modifier.size(16.dp)) }, label = stringResource(R.string.label_record_text))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = record.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(R.string.label_language_format, record.language),
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceDim,
                    )
                }

                is NdefRecord.UriRecord -> {
                    RecordHeader(icon = { Icon(Icons.Default.Link, null, Modifier.size(16.dp)) }, label = stringResource(R.string.label_record_uri))
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = { onUriClick(record.uri) }) {
                        Text(
                            text = record.uri,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                            ),
                            color = NfcAmber,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                is NdefRecord.MimeRecord -> {
                    RecordHeader(icon = { Icon(Icons.Default.Memory, null, Modifier.size(16.dp)) }, label = stringResource(R.string.label_record_mime))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = record.mimeType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(R.string.label_bytes_format, record.data.size),
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceDim,
                    )
                }

                is NdefRecord.ExternalRecord -> {
                    RecordHeader(icon = { Icon(Icons.Default.Memory, null, Modifier.size(16.dp)) }, label = stringResource(R.string.label_record_external))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${record.domain}:${record.type}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(R.string.label_bytes_format, record.data.size),
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceDim,
                    )
                }

                is NdefRecord.UnknownRecord -> {
                    RecordHeader(icon = { Icon(Icons.Default.Memory, null, Modifier.size(16.dp)) }, label = stringResource(R.string.label_record_unknown))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.label_bytes_format, record.payload.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordHeader(
    icon: @Composable () -> Unit,
    label: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = OnSurfaceMedium,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun NfcTagCardPreview() {
    WirelessExplorerTheme {
        NfcTagCard(
            tagData = NfcTagData(
                id = "04:A2:1B:3C:5D:6E:7F",
                techList = listOf("NfcA", "Ndef", "MifareUltralight"),
                ndefRecords = listOf(
                    NdefRecord.TextRecord(text = "Hello NFC", language = "en"),
                    NdefRecord.UriRecord(uri = "https://example.com"),
                ),
            ),
        )
    }
}
