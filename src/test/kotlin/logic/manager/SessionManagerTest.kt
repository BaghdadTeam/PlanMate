package logic.manager

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.repositories.SessionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `getActiveSession() returns result with failer if there is no active session`() {
        // Given
        every { sessionRepository.loadSession() }.returns(null)
        // When
        val result = sessionManager.getActiveSession()
        // Then
        assertThat(result.isFailure).isTrue()
    }
    @Test
    fun `getActiveSession() returns result with pass if session exists`() {
        // Given
        every { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
        val result = sessionManager.getActiveSession()
        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `clearExpiredSession() clears expired session`() {
        every { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        sessionManager.clearExpiredSession()
        verify { sessionRepository.deleteSession() }
    }
    @Test
    fun `getActiveSession() returns result failure if there is no active session`() {
        // Given
        every { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        // When
        val result = sessionManager.getActiveSession()
        // Then
        assertThat(result.isFailure).isTrue()
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