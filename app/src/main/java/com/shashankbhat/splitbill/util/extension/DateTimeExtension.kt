package com.shashankbhat.splitbill.util

import android.content.Context
import android.text.format.DateUtils

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun Context.getTimeAgo(time: Long): String {
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return ""
    }

    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> "just now"
        diff < 2 * MINUTE_MILLIS -> "a minute ago"
        diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
        diff < 2 * HOUR_MILLIS -> "an hour ago"
        diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> DateUtils.getRelativeDateTimeString(
            this, time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            DateUtils.FORMAT_SHOW_TIME
        ).toString()
    }
}