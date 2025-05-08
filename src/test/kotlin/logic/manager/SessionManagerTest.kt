package logic.manager

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `clearExpiredSession() clears expired session`() = runTest {
        // Given
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSessionWithExpiredDate
        // When
        sessionManager.clearExpiredSession()
        // Then
        coVerify { sessionRepository.deleteSession() }
    }

    @Test
    fun `clearExpiredSession() should not do anything if session is not expired`() = runTest {
        // Given
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
        sessionManager.clearExpiredSession()
        // Then
        coVerify(exactly = 0) { sessionRepository.deleteSession() }
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
    fun `clearExpiredSession() should not delete session if session is not expired`() = runTest {
        // Given
        coEvery { sessionRepository.loadSession() } returns SessionTestData.baseSession
        // When
        sessionManager.clearExpiredSession()
        // Then
        coVerify(exactly = 0) { sessionRepository.deleteSession() }
    }
}
