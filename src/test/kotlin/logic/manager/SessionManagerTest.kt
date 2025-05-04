package logic.manager

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.logic.repositories.SessionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SessionManagerTest {
    lateinit var sessionManager: SessionManager
    lateinit var sessionRepository: SessionRepository

    @BeforeEach
    fun setUp() {
        sessionRepository = mockk(relaxed = true)
        sessionManager = SessionManager(sessionRepository)
    }
    @Test
    fun `getActiveSession() throw SessionNotFoundException if there is no active session`() {
        // Given
        every { sessionRepository.loadSession() }.returns(null)
        // When
        // Then
        assertThrows<SessionNotFoundException> { sessionManager.getActiveSession() }
    }
    @Test
    fun `getActiveSession() returns SessionEntity if session exists`() {
        // Given
        every { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
        val result = sessionManager.getActiveSession()
        // Then
        assertThat(result.id).isEqualTo(SessionTestData.baseSession.id)
    }

    @Test
    fun `clearExpiredSession() clears expired session`() {
        every { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        sessionManager.clearExpiredSession()
        verify { sessionRepository.deleteSession() }
    }
    @Test
    fun `getActiveSession() throws SessionEndedException if there is no active session`() {
        // Given
        every { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        // When & Then
        assertThrows<SessionEndedException> { sessionManager.getActiveSession() }
    }
    @Test
    fun `clearExpiredSession() should not do anything if there is no expired session`() {
        // Given
        every { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
sessionManager.clearExpiredSession()
        // Then
        verify(exactly = 0) { sessionRepository.deleteSession() }

    }
    @Test
    fun `setSession correctly sets the currentSession`() {
        // Given
        val session = SessionTestData.baseSession
        // When
        sessionManager.setSession(session)
        // Then
        assertEquals(session, sessionManager.currentSession)
    }

    @Test
    fun `clearSession() should check if there is no session`() {
        // Given
        every { sessionRepository.loadSession() }.returns(null)
        // When
        sessionManager.clearExpiredSession()
        // Then
        verify (exactly = 0){ sessionRepository.deleteSession() }
    }
}