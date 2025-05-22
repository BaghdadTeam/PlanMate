package org.baghdad.presentation.utils

import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun formatTimestamp(currentTimestamp: LocalDateTime): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a", Locale.ENGLISH)
        currentTimestamp.format(formatter)
    } catch (_: DateTimeParseException) {
        throw UnSupportedTimeStampFormatException()
    }
}