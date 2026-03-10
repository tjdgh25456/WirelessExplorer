package com.jsh.wirelessexplorer.presentation.devices.uwb

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.domain.model.UwbRangingResult
import com.jsh.wirelessexplorer.domain.model.UwbState
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceMedium
import com.jsh.wirelessexplorer.presentation.theme.Surface
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.UwbViolet
import androidx.compose.ui.tooling.preview.Preview
import com.jsh.wirelessexplorer.presentation.common.RequirePermissions
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import java.util.Locale

private const val DISTANCE_CLOSE_THRESHOLD = 0.5f
private const val DISTANCE_MEDIUM_THRESHOLD = 2f
private const val DISTANCE_DISPLAY_FONT_SIZE = 48

@Composable
fun UwbScreen(
    viewModel: UwbViewModel = hiltViewModel(),
) {
    // UWB 레인징 권한 확인 후 콘텐츠 표시
    RequirePermissions(
        permissions = listOf(Manifest.permission.UWB_RANGING),
        rationaleTitle = stringResource(R.string.title_uwb_permission),
        rationaleMessage = stringResource(R.string.desc_uwb_permission),
    ) {
        UwbScreenContent(viewModel = viewModel)
    }
}

@Composable
private fun UwbScreenContent(
    viewModel: UwbViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (val state = uiState.state) {
            is UwbState.NotSupported -> NotSupportedContent()

            is UwbState.Idle -> IdleContent(onStartRanging = viewModel::startRanging)

            is UwbState.Searching -> SearchingContent()

            is UwbState.Ranging -> RangingContent(
                result = state.result,
                onStop = viewModel::stopRanging,
            )

            is UwbState.Error -> ErrorContent(
                message = state.message,
                onRetry = viewModel::startRanging,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NotSupportedContent() {
    Spacer(modifier = Modifier.height(80.dp))

    Icon(
        imageVector = Icons.Default.Bluetooth,
        contentDescription = stringResource(R.string.label_uwb),
        modifier = Modifier.size(64.dp),
        tint = Color.Gray,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(R.string.title_uwb_not_supported),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.desc_uwb_not_supported),
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(32.dp))

    // UWB 지원 디바이스 목록
    Text(
        text = stringResource(R.string.label_supported_devices),
        style = MaterialTheme.typography.labelSmall,
        color = OnSurfaceDim,
    )

    Spacer(modifier = Modifier.height(12.dp))

    val supportedDevices = listOf(
        "Galaxy S21 Ultra",
        "Galaxy S22+",
        "Galaxy S23 Ultra",
        "Galaxy Z Fold4",
        "Pixel 6 Pro",
        "Pixel 7 Pro",
        "Pixel 8 Pro",
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        supportedDevices.forEach { device ->
            SuggestionChip(
                onClick = {},
                label = {
                    Text(text = device, style = MaterialTheme.typography.labelSmall)
                },
                icon = {
                    Icon(
                        Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = SurfaceVariant,
                    labelColor = OnSurfaceMedium,
                    iconContentColor = UwbViolet,
                ),
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedButton(
        onClick = {},
        colors = ButtonDefaults.outlinedButtonColors(contentColor = UwbViolet),
    ) {
        Text(stringResource(R.string.btn_check_compatibility))
    }
}

@Composable
private fun IdleContent(onStartRanging: () -> Unit) {
    Spacer(modifier = Modifier.height(80.dp))

    Icon(
        imageVector = Icons.Default.Bluetooth,
        contentDescription = stringResource(R.string.label_uwb),
        modifier = Modifier.size(64.dp),
        tint = UwbViolet,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(R.string.title_uwb_ready),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.desc_uwb_ready),
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = onStartRanging,
        colors = ButtonDefaults.buttonColors(containerColor = UwbViolet),
    ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.btn_start_ranging))
    }
}

@Composable
private fun SearchingContent() {
    Spacer(modifier = Modifier.height(80.dp))

    // 간단한 searching 상태 표시
    Text(
        text = stringResource(R.string.label_searching),
        style = MaterialTheme.typography.titleLarge,
        color = UwbViolet,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.desc_searching_uwb),
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
    )
}

@Composable
private fun RangingContent(
    result: UwbRangingResult,
    onStop: () -> Unit,
) {
    // Radar
    RadarView(
        rangingResult = result,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )

    Spacer(modifier = Modifier.height(16.dp))

    // 거리 표시
    val distanceColor = when {
        result.distanceMeters < DISTANCE_CLOSE_THRESHOLD -> Color(0xFFEF4444)
        result.distanceMeters < DISTANCE_MEDIUM_THRESHOLD -> Color(0xFF22C55E)
        else -> Color(0xFF3B82F6)
    }

    Text(
        text = String.format(Locale.US, "%.2f m", result.distanceMeters),
        style = MaterialTheme.typography.displayLarge.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = DISTANCE_DISPLAY_FONT_SIZE.sp,
        ),
        color = distanceColor,
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Stats bar
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StatItem(
                label = stringResource(R.string.label_azimuth),
                value = String.format(Locale.US, "%.1f\u00B0", result.azimuthDegrees),
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(SurfaceVariant),
            )
            StatItem(
                label = stringResource(R.string.label_elevation),
                value = String.format(Locale.US, "%.1f\u00B0", result.elevationDegrees),
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(SurfaceVariant),
            )
            StatItem(
                label = stringResource(R.string.label_distance),
                value = String.format(Locale.US, "%.2f m", result.distanceMeters),
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onStop,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
    ) {
        Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.btn_stop_ranging))
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceDim,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
            ),
            color = UwbViolet,
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Spacer(modifier = Modifier.height(80.dp))

    Icon(
        imageVector = Icons.Default.ErrorOutline,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.error,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(R.string.title_ranging_error),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = OnSurfaceDim,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onRetry,
        colors = ButtonDefaults.buttonColors(containerColor = UwbViolet),
    ) {
        Text(stringResource(R.string.btn_retry))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun UwbScreenIdlePreview() {
    WirelessExplorerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IdleContent(onStartRanging = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun UwbScreenRangingPreview() {
    WirelessExplorerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RangingContent(
                result = UwbRangingResult(
                    distanceMeters = 1.35f,
                    azimuthDegrees = 15.2f,
                    elevationDegrees = -5.0f,
                ),
                onStop = {},
            )
        }
    }
}
