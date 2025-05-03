package utils

import helpers.utils.fixedClock
import kotlinx.datetime.*
import org.baghdad.utils.getFormattedTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeUtilsTest {

    @Test
    fun `returns correct AM format with midnight`() {
        val time = LocalDateTime(2024, 5, 1, 0, 7) // 12:07 AM
        val result = getFormattedTimestamp(fixedClock(time))
        assertEquals("2024/05/01 12:07 AM", result)
    }

    @Test
    fun `returns correct AM format`() {
        val time = LocalDateTime(2024, 5, 1, 9, 5) // 9:05 AM
        val result = getFormattedTimestamp(fixedClock(time))
        assertEquals("2024/05/01 9:05 AM", result)
    }

    @Test
    fun `returns correct PM format with noon`() {
        val time = LocalDateTime(2024, 5, 1, 12, 15) // 12:15 PM
        val result = getFormattedTimestamp(fixedClock(time))
        assertEquals("2024/05/01 12:15 PM", result)
    }

    @Test
    fun `returns correct PM format`() {
        val time = LocalDateTime(2024, 5, 1, 18, 45) // 6:45 PM
        val result = getFormattedTimestamp(fixedClock(time))
        assertEquals("2024/05/01 6:45 PM", result)
    }

    @Test
    fun `pads single digit day and month correctly`() {
        val time = LocalDateTime(2024, 3, 4, 10, 5) // 10:05 AM
        val result = getFormattedTimestamp(fixedClock(time))
        assertEquals("2024/03/04 10:05 AM", result)
    }
}