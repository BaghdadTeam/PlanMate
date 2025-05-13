package org.baghdad.logic.model.entities

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class SessionEntityTest {

    @Test
    fun `isExpired should return false when session is within expiration time`() {
        // Given
        val recentLogin = LocalDateTime.now().minusMinutes(10)
        val session = SessionEntity(
            userId = UUID.randomUUID(),
            token = "token123",
            loginTime = recentLogin
        )
        // When
        val result = session.isExpired()
        // Then
        assertFalse(result)
    }

    @Test
    fun `isExpired should return true when session is past expiration time`() {
        // Given
        val oldLogin = LocalDateTime.now().minusMinutes(45)
        val session = SessionEntity(
            userId = UUID.randomUUID(),
            token = "token123",
            loginTime = oldLogin
        )
        // When
        val result = session.isExpired()

        // Then
        assertTrue(result)
    }
}