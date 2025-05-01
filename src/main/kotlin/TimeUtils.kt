package org.baghdad

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getFormattedTimestamp(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val year = now.year.toString().padStart(4, '0')
    val month = now.monthNumber.toString().padStart(2, '0')
    val day = now.dayOfMonth.toString().padStart(2, '0')

    val hour12 = if (now.hour % 12 == 0) 12 else now.hour % 12
    val minute = now.minute.toString().padStart(2, '0')
    val amPm = if (now.hour < 12) "AM" else "PM"

    return "$year/$month/$day $hour12:$minute $amPm"
}