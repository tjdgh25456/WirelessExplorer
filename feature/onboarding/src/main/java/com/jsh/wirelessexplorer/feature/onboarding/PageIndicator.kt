package com.jsh.wirelessexplorer.feature.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurfaceDim
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary

private const val INDICATOR_ANIMATION_DURATION_MS = 300

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            val width by animateDpAsState(
                targetValue = if (selected) 24.dp else 8.dp,
                animationSpec = tween(INDICATOR_ANIMATION_DURATION_MS),
                label = "indicatorWidth",
            )
            val color by animateColorAsState(
                targetValue = if (selected) Primary else OnSurfaceDim,
                animationSpec = tween(INDICATOR_ANIMATION_DURATION_MS),
                label = "indicatorColor",
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
