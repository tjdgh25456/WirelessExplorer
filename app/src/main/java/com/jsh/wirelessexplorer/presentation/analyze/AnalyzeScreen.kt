package com.jsh.wirelessexplorer.presentation.analyze

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsh.wirelessexplorer.domain.model.AnalyzePeriod
import com.jsh.wirelessexplorer.presentation.theme.Background
import com.jsh.wirelessexplorer.presentation.theme.BleBlue
import com.jsh.wirelessexplorer.presentation.theme.OnPrimary
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.Primary
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AnalyzeScreen(
    viewModel: AnalyzeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Primary, modifier = Modifier.size(40.dp))
        }
        return
    }

    val hasData = state.summary.deviceCount > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp, bottom = 24.dp),
    ) {
        // 기간 필터 칩
        PeriodFilterChips(
            selectedPeriod = state.selectedPeriod,
            onPeriodSelected = viewModel::selectPeriod,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!hasData) {
            EmptyState()
            return
        }

        // 요약 카드 3개
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SummaryCard(
                icon = Icons.Default.Devices,
                value = "${state.summary.deviceCount}",
                label = stringResource(R.string.label_devices),
                iconTint = BleBlue,
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                icon = Icons.Default.LinkOff,
                value = "${state.summary.totalConnections}",
                label = stringResource(R.string.label_connections),
                iconTint = WifiGreen,
                modifier = Modifier.weight(1f),
            )
            SummaryCard(
                icon = Icons.Default.SignalCellularAlt,
                value = state.summary.bestSignal?.let { stringResource(R.string.label_dbm_format, it) } ?: stringResource(R.string.label_dbm_placeholder),
                label = stringResource(R.string.label_best_signal),
                iconTint = Primary,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 도넛 차트
        DonutChart(
            distribution = state.techDistribution,
            totalDevices = state.summary.deviceCount,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 상위 디바이스
        TopDevicesSection(devices = state.topDevices)
    }
}

@Composable
private fun PeriodFilterChips(
    selectedPeriod: AnalyzePeriod,
    onPeriodSelected: (AnalyzePeriod) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AnalyzePeriod.entries.forEach { period ->
            val selected = period == selectedPeriod
            FilterChip(
                selected = selected,
                onClick = { onPeriodSelected(period) },
                label = {
                    Text(
                        text = period.displayName,
                        fontSize = 12.sp,
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Primary,
                    selectedLabelColor = OnPrimary,
                    containerColor = SurfaceVariant,
                    labelColor = OnSurfaceDim,
                ),
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Devices,
                contentDescription = null,
                tint = OnSurfaceDim,
                modifier = Modifier.size(56.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.empty_no_data_title),
                color = OnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.empty_no_data_desc),
                color = OnSurfaceDim,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun AnalyzeScreenPreview() {
    WirelessExplorerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 24.dp),
        ) {
            PeriodFilterChips(
                selectedPeriod = AnalyzePeriod.LAST_7_DAYS,
                onPeriodSelected = {},
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SummaryCard(
                    icon = Icons.Default.Devices,
                    value = "12",
                    label = "Devices",
                    iconTint = BleBlue,
                    modifier = Modifier.weight(1f),
                )
                SummaryCard(
                    icon = Icons.Default.LinkOff,
                    value = "48",
                    label = "Connections",
                    iconTint = WifiGreen,
                    modifier = Modifier.weight(1f),
                )
                SummaryCard(
                    icon = Icons.Default.SignalCellularAlt,
                    value = "-35 dBm",
                    label = "Best Signal",
                    iconTint = Primary,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
