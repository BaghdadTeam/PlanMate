package data.utils

import com.google.common.truth.Truth.assertThat
import org.baghdad.data.utils.parseTimestamp
import org.baghdad.logic.model.exceptions.UnSupportedTimeStampFormatException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class DateTime {

    @Test
    fun `should throw UnSupportedTimeStampFormatException when invalid timestamp format`() {
        // Given
        val invalidTimestamp = "2023-09-15"

        // When & Then
        assertThrows<UnSupportedTimeStampFormatException> {
            parseTimestamp(invalidTimestamp)
        }
    }

    @Test
    fun `should return true when parse valid timestamp`() {
        // Given
        val validTimestamp = LocalDateTime.now().toString()

        // When
        val parsedTimestamp = parseTimestamp(validTimestamp)

        // Then
        assertThat(parsedTimestamp.toString() == validTimestamp).isTrue()
    }
}