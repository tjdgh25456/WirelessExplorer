package com.jsh.wirelessexplorer.feature.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.jsh.wirelessexplorer.core.designsystem.theme.OnSurface
import com.jsh.wirelessexplorer.core.designsystem.theme.Primary

/**
 * 검색어에 해당하는 부분을 강조 색상으로 표시하는 텍스트.
 * 대소문자 구분 없이 매칭한다.
 */
@Composable
fun HighlightedText(
    text: String,
    highlight: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 15.sp,
) {
    if (highlight.isBlank()) {
        Text(
            text = text,
            color = OnSurface,
            fontSize = fontSize,
            modifier = modifier,
        )
        return
    }

    val annotated = remember(text, highlight) {
        buildAnnotatedString {
            var startIndex = 0
            val lowerText = text.lowercase()
            val lowerHighlight = highlight.lowercase()

            while (startIndex < text.length) {
                val matchIndex = lowerText.indexOf(lowerHighlight, startIndex)
                if (matchIndex == -1) {
                    // 남은 텍스트
                    withStyle(SpanStyle(color = OnSurface)) {
                        append(text.substring(startIndex))
                    }
                    break
                }

                // 매칭 전 텍스트
                if (matchIndex > startIndex) {
                    withStyle(SpanStyle(color = OnSurface)) {
                        append(text.substring(startIndex, matchIndex))
                    }
                }

                // 매칭 부분 강조
                withStyle(SpanStyle(color = Primary, fontWeight = FontWeight.SemiBold)) {
                    append(text.substring(matchIndex, matchIndex + highlight.length))
                }

                startIndex = matchIndex + highlight.length
            }
        }
    }

    Text(
        text = annotated,
        fontSize = fontSize,
        modifier = modifier,
    )
}
