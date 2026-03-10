package com.jsh.wirelessexplorer.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.R
import com.jsh.wirelessexplorer.domain.model.SignalStrength
import com.jsh.wirelessexplorer.presentation.theme.Error
import com.jsh.wirelessexplorer.presentation.theme.NfcAmber
import com.jsh.wirelessexplorer.presentation.theme.OnSurface
import com.jsh.wirelessexplorer.presentation.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.presentation.theme.SurfaceVariant
import com.jsh.wirelessexplorer.presentation.theme.WifiGreen

private const val RSSI_MIN = -100f
private const val RSSI_MAX = -20f
private const val ARC_START_ANGLE = 135f
private const val ARC_SWEEP_ANGLE = 270f
private val GAUGE_SIZE = 200.dp
private val GAUGE_STROKE_WIDTH = 16.dp
private const val QUALITY_LABEL_FONT_SIZE = 13

/**
 * RSSI 값을 시각적 원형 게이지로 표시한다.
 * -100 ~ -20 dBm 범위를 270도 아크로 매핑한다.
 */
@Composable
fun RssiGauge(
    rssi: Int,
    modifier: Modifier = Modifier,
) {
    val signalStrength = SignalStrength.from(rssi)
    val qualityLabel = when (signalStrength) {
        SignalStrength.EXCELLENT -> stringResource(R.string.label_signal_excellent)
        SignalStrength.GOOD -> stringResource(R.string.label_signal_good)
        SignalStrength.FAIR -> stringResource(R.string.label_signal_fair)
        SignalStrength.POOR -> stringResource(R.string.label_signal_poor)
    }

    // RSSI를 0~1 범위로 정규화
    val normalizedValue = ((rssi - RSSI_MIN) / (RSSI_MAX - RSSI_MIN)).coerceIn(0f, 1f)
    val sweepAngle = ARC_SWEEP_ANGLE * normalizedValue

    // 신호 품질에 따른 그라데이션 색상
    val gradientColors = listOf(Error, NfcAmber, WifiGreen)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(GAUGE_SIZE),
    ) {
        Canvas(modifier = Modifier.size(GAUGE_SIZE)) {
            val strokeWidth = GAUGE_STROKE_WIDTH.toPx()
            val padding = strokeWidth / 2
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(padding, padding)

            // 배경 아크
            drawArc(
                color = SurfaceVariant,
                startAngle = ARC_START_ANGLE,
                sweepAngle = ARC_SWEEP_ANGLE,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )

            // 전경 그라데이션 아크
            if (sweepAngle > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(gradientColors),
                    startAngle = ARC_START_ANGLE,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
        }

        // 중앙 텍스트
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$rssi",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurface,
            )
            Text(
                text = stringResource(R.string.label_dbm),
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceDim,
            )
            Text(
                text = qualityLabel,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = QUALITY_LABEL_FONT_SIZE.sp,
                ),
                color = when (signalStrength) {
                    SignalStrength.EXCELLENT -> WifiGreen
                    SignalStrength.GOOD -> WifiGreen
                    SignalStrength.FAIR -> NfcAmber
                    SignalStrength.POOR -> Error
                },
            )
        }
    }
}
