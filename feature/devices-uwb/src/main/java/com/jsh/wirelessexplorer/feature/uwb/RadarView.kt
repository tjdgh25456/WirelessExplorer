package com.jsh.wirelessexplorer.feature.uwb

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.jsh.wirelessexplorer.core.model.UwbRangingResult
import com.jsh.wirelessexplorer.core.designsystem.theme.UwbViolet
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val MAX_RANGE_METERS = 6f
private const val RING_COUNT = 4
private const val DISTANCE_CLOSE_THRESHOLD = 0.5f
private const val DISTANCE_MEDIUM_THRESHOLD = 2f
private const val PULSE_DURATION_MS = 2000
private const val SELF_DOT_RADIUS = 6f
private const val PARTNER_GLOW_RADIUS = 18f
private const val PARTNER_DOT_RADIUS = 8f
private const val PARTNER_CENTER_RADIUS = 3f

/**
 * UWB 레인징 결과를 시각화하는 레이더 뷰.
 * 동심원 + 중앙 자기 위치(흰색) + 상대 디바이스(주황색) 표시.
 */
@Composable
fun RadarView(
    rangingResult: UwbRangingResult?,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar_pulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = PULSE_DURATION_MS),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxRadius = min(size.width, size.height) / 2f * 0.9f

        // 동심원
        for (i in 1..RING_COUNT) {
            val fraction = i.toFloat() / RING_COUNT
            val ringRadius = maxRadius * fraction
            val alpha = 0.15f + (1f - fraction) * 0.1f

            drawCircle(
                color = UwbViolet.copy(alpha = alpha),
                radius = ringRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f),
            )
        }

        // 펄스 링
        val pulseRadius = maxRadius * pulseProgress
        val pulseAlpha = (1f - pulseProgress) * 0.3f
        drawCircle(
            color = UwbViolet.copy(alpha = pulseAlpha),
            radius = pulseRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f),
        )

        // 십자선
        drawLine(
            color = UwbViolet.copy(alpha = 0.1f),
            start = Offset(centerX, centerY - maxRadius),
            end = Offset(centerX, centerY + maxRadius),
            strokeWidth = 1f,
        )
        drawLine(
            color = UwbViolet.copy(alpha = 0.1f),
            start = Offset(centerX - maxRadius, centerY),
            end = Offset(centerX + maxRadius, centerY),
            strokeWidth = 1f,
        )

        // 자기 위치 (중앙 흰색 점)
        drawCircle(
            color = Color.White,
            radius = SELF_DOT_RADIUS,
            center = Offset(centerX, centerY),
        )

        // 상대 디바이스 위치
        if (rangingResult != null) {
            drawPartnerDot(
                centerX = centerX,
                centerY = centerY,
                maxRadius = maxRadius,
                result = rangingResult,
            )
        }
    }
}

private fun DrawScope.drawPartnerDot(
    centerX: Float,
    centerY: Float,
    maxRadius: Float,
    result: UwbRangingResult,
) {
    // 거리를 레이더 반경으로 매핑
    val normalizedDistance = (result.distanceMeters / MAX_RANGE_METERS).coerceIn(0f, 1f)
    val pixelRadius = normalizedDistance * maxRadius

    // azimuth를 라디안으로 변환 (0도=위쪽, 시계 방향)
    val angleRad = Math.toRadians((result.azimuthDegrees - 90.0)).toFloat()
    val dotX = centerX + pixelRadius * cos(angleRad)
    val dotY = centerY + pixelRadius * sin(angleRad)

    // 거리에 따른 색상
    val dotColor = when {
        result.distanceMeters < DISTANCE_CLOSE_THRESHOLD -> Color(0xFFEF4444) // 근접: 빨강
        result.distanceMeters < DISTANCE_MEDIUM_THRESHOLD -> Color(0xFF22C55E)    // 중간: 초록
        else -> Color(0xFF3B82F6)                          // 원거리: 파랑
    }

    // Glow
    drawCircle(
        color = dotColor.copy(alpha = 0.25f),
        radius = PARTNER_GLOW_RADIUS,
        center = Offset(dotX, dotY),
    )

    // Dot
    drawCircle(
        color = dotColor,
        radius = PARTNER_DOT_RADIUS,
        center = Offset(dotX, dotY),
    )

    // Bright center
    drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = PARTNER_CENTER_RADIUS,
        center = Offset(dotX, dotY),
    )
}
