package com.jsh.wirelessexplorer.presentation.devices.ble

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

/**
 * RSSI 신호 강도를 1~4개 바로 시각화하는 컴포저블.
 *
 * - 4 bars (>= -50 dBm): 녹색
 * - 3 bars (-50 ~ -70): 녹색
 * - 2 bars (-70 ~ -85): 주황
 * - 1 bar  (< -85):     빨강
 */
@Composable
fun SignalBars(
    rssi: Int,
    modifier: Modifier = Modifier,
) {
    val barCount = when {
        rssi >= -50 -> 4
        rssi >= -70 -> 3
        rssi >= -85 -> 2
        else -> 1
    }

    val activeColor = when (barCount) {
        4, 3 -> WifiGreen
        2 -> Color(0xFFF59E0B) // Amber
        else -> Error
    }

    Canvas(modifier = modifier.size(width = 24.dp, height = 20.dp)) {
        val totalBars = 4
        val barWidth = size.width / (totalBars * 2f - 1f)
        val cornerRadius = CornerRadius(2f, 2f)

        for (i in 0 until totalBars) {
            val barHeight = size.height * (0.25f + i * 0.25f)
            val x = i * barWidth * 2f
            val y = size.height - barHeight
            val color = if (i < barCount) activeColor else OnSurfaceDim.copy(alpha = 0.3f)

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = cornerRadius,
            )
        }
    }
}
