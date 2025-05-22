package org.baghdad.data.utils

import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


fun parseTimestamp(timestampString: String): LocalDateTime {
    return try {
        LocalDateTime.parse(timestampString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } catch (_: DateTimeParseException) {
        throw UnSupportedTimeStampFormatException()
    }
}

