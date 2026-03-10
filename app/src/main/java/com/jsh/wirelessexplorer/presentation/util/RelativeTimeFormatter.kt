package com.jsh.wirelessexplorer.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** 타임스탬프를 상대 시간 문자열로 변환한다. */
object RelativeTimeFormatter {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        if (diff < 0) return dateFormat.format(Date(timestamp))

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days == 1L -> "Yesterday"
            days < 30 -> "${days}d ago"
            else -> dateFormat.format(Date(timestamp))
        }
    }
}
