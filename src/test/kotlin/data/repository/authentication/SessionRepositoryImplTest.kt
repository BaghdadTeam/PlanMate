package data.repository.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.local.SessionDataSource
import org.baghdad.data.repository.authentication.SessionRepositoryImpl
import org.junit.jupiter.api.*

class SessionRepositoryImplTest {
    private lateinit var repository: SessionRepositoryImpl
    private lateinit var sessionDataSource: SessionDataSource

    @BeforeEach
    fun setUp() {
        sessionDataSource = mockk(relaxed = true)
        repository = SessionRepositoryImpl(sessionDataSource)
    }
    @Test
    fun `Should invoke sessionDataSource saveSession when saveSession is called`() {
        // When
        repository.saveSession(SessionTestData.baseSession)
        // Then
        verify { sessionDataSource.saveSession(any()) }
    }
    @Test
    fun `Should return true if session deleted successfully`() {
        // Given
        every { sessionDataSource.deleteSession() } returns true
        // When
        val result = repository.deleteSession()
        // Then
        assertThat(result).isTrue()
    }
    @Test
    fun `Should return false if session couldn't be deleted `() {
        // Given
        every { sessionDataSource .deleteSession() } returns false
        // When
        val result = repository.deleteSession()
        // Then
        assertThat(result).isFalse()
    }
    @Test
    fun `Should return not expired Session when loadSession is called`() {
        // Given
        every { sessionDataSource.loadSession() } returns SessionTestData.baseSession
        // When
        val result = repository.loadSession()
        // Then
        assertThat(result?.isExpired()).isFalse()

    }
}