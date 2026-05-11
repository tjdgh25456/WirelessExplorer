package com.jsh.wirelessexplorer.feature.analyze

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import com.jsh.wirelessexplorer.feature.analyze.R
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.model.TechDistribution
import com.jsh.wirelessexplorer.core.model.TechType
import com.jsh.wirelessexplorer.core.designsystem.theme.BleBlue
import com.jsh.wirelessexplorer.core.designsystem.theme.NfcAmber
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.SurfaceVariant
import com.jsh.wirelessexplorer.core.designsystem.theme.UwbViolet
import com.jsh.wirelessexplorer.core.designsystem.theme.WifiGreen

private const val STROKE_WIDTH_DP = 40f
private const val DONUT_SIZE_DP = 200
private const val DONUT_ANIMATION_DURATION_MS = 800

/** 기술별 디바이스 분포를 보여주는 도넛 차트 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonutChart(
    distribution: List<TechDistribution>,
    totalDevices: Int,
    modifier: Modifier = Modifier,
) {
    // 애니메이션 진행 값 (0 → 1)
    var animProgress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animProgress,
        animationSpec = tween(durationMillis = DONUT_ANIMATION_DURATION_MS),
        label = "donutAnimation",
    )
    LaunchedEffect(distribution) {
        animProgress = 0f
        animProgress = 1f
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(DONUT_SIZE_DP.dp),
        ) {
            Canvas(modifier = Modifier.size(DONUT_SIZE_DP.dp)) {
                val strokeWidth = STROKE_WIDTH_DP.dp.toPx()
                val arcSize = Size(
                    width = size.width - strokeWidth,
                    height = size.height - strokeWidth,
                )
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                if (distribution.isEmpty()) {
                    // 데이터 없을 때 빈 링 표시
                    drawArc(
                        color = SurfaceVariant,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    )
                    return@Canvas
                }

                var currentAngle = -90f // 12시 방향에서 시작
                distribution.forEach { item ->
                    val sweep = item.percentage * 360f * animatedProgress
                    drawArc(
                        color = item.techType.chartColor(),
                        startAngle = currentAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    )
                    currentAngle += sweep
                }
            }

            // 중앙 총 디바이스 수
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$totalDevices",
                    color = OnSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.label_devices),
                    color = OnSurfaceDim,
                    fontSize = 12.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 범례
        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            distribution.forEach { item ->
                LegendItem(
                    color = item.techType.chartColor(),
                    label = item.techType.label,
                    percentage = "${(item.percentage * 100).toInt()}%",
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    percentage: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = OnSurface, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = percentage, color = OnSurfaceDim, fontSize = 12.sp)
    }
}

private fun TechType.chartColor(): Color = when (this) {
    TechType.BLE -> BleBlue
    TechType.WIFI -> WifiGreen
    TechType.NFC -> NfcAmber
    TechType.UWB -> UwbViolet
}
