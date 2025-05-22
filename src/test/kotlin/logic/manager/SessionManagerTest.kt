package logic.manager

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.SessionEndedException
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
    fun `getActiveSession() throws SessionEndedException if session is expired`() = runTest {
        // Given
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        // When & Then
        assertThrows<SessionEndedException> { sessionManager.getActiveSession() }
    }

    @Test
    fun `getActiveSession() returns SessionEntity if session is active and not expired`() = runTest {
        // Given
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
        val result = sessionManager.getActiveSession()
        // Then
        assertThat(result.id).isEqualTo(SessionTestData.baseSession.id)
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
    fun `isAuthenticated should return true where there is active session `() = runTest {
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSession
        val result = sessionManager.isAuthenticated()
        assertThat(result).isTrue()
    }

    @Test
    fun `isAuthenticated should return false if there is no active session `() = runTest {
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        val result = sessionManager.isAuthenticated()
        assertThat(result).isFalse()
    }
}