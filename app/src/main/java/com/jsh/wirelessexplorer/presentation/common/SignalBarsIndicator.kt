package com.jsh.wirelessexplorer.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.presentation.theme.Error
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen
import com.jsh.wirelessexplorer.presentation.theme.WirelessExplorerTheme
import androidx.compose.ui.tooling.preview.Preview

private const val RSSI_EXCELLENT = -50
private const val RSSI_GOOD = -70
private const val RSSI_FAIR = -85
private const val TOTAL_BARS = 4

private val YellowGreen = Color(0xFF84CC16)
private val Orange = Color(0xFFF59E0B)

/**
 * RSSI 기반 신호 강도 표시 인디케이터.
 *
 * 바 개수와 색상:
 * - 4 bars (>= -50 dBm): 녹색
 * - 3 bars (-50 ~ -70): 연두색
 * - 2 bars (-70 ~ -85): 주황색
 * - 1 bar  (< -85):     빨강색
 */
@Composable
fun SignalBarsIndicator(
    rssi: Int,
    modifier: Modifier = Modifier,
) {
    val filledCount = when {
        rssi >= RSSI_EXCELLENT -> 4
        rssi >= RSSI_GOOD -> 3
        rssi >= RSSI_FAIR -> 2
        else -> 1
    }

    val activeColor = when (filledCount) {
        4 -> WifiGreen
        3 -> YellowGreen
        2 -> Orange
        else -> Error
    }

    Canvas(modifier = modifier.size(width = 24.dp, height = 20.dp)) {
        val barWidth = size.width / (TOTAL_BARS * 2f - 1f)
        val cornerRadius = CornerRadius(2f, 2f)

        for (i in 0 until TOTAL_BARS) {
            val barHeight = size.height * (0.25f + i * 0.25f)
            val x = i * barWidth * 2f
            val y = size.height - barHeight
            val color = if (i < filledCount) activeColor else OnSurfaceDim.copy(alpha = 0.3f)

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = cornerRadius,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A1A)
@Composable
private fun SignalBarsIndicatorPreview() {
    WirelessExplorerTheme {
        SignalBarsIndicator(rssi = -45)
    }
}
