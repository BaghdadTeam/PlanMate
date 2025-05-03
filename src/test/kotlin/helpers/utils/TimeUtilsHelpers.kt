package helpers.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class FixedClock(private val fixedInstant: Instant) : Clock {
    override fun now(): Instant = fixedInstant
}

fun fixedClock(dateTime: LocalDateTime): Clock {
    return FixedClock(dateTime.toInstant(TimeZone.currentSystemDefault()))
}
