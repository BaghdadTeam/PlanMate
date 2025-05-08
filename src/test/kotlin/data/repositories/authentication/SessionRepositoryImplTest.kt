package data.repositories.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.SessionDataSource
import org.baghdad.data.repositories.authentication.SessionRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SessionRepositoryImplTest {
    private lateinit var repository: SessionRepositoryImpl
    private lateinit var sessionDataSource: SessionDataSource

    @BeforeEach
    fun setUp() {
        sessionDataSource = mockk(relaxed = true)
        repository = SessionRepositoryImpl(sessionDataSource)
    }
    @Test
    fun `Should invoke sessionDataSource saveSession when saveSession is called`()= runTest {
        // When
        repository.saveSession(SessionTestData.baseSession)
        // Then
        coVerify { sessionDataSource.saveSession(any()) }
    }
    @Test
    fun `Should return true if session deleted successfully`() = runTest{
        // Given
        coEvery { sessionDataSource.deleteSession() } returns true
        // When
        val result = repository.deleteSession()
        // Then
        assertThat(result).isTrue()
    }
    @Test
    fun `Should return false if session couldn't be deleted `() = runTest{
        // Given
        coEvery { sessionDataSource .deleteSession() } returns false
        // When
        val result = repository.deleteSession()
        // Then
        assertThat(result).isFalse()
    }
    @Test
    fun `Should return not expired Session when loadSession is called`() = runTest{
        // Given
        coEvery { sessionDataSource.loadSession() } returns SessionTestData.baseSession
        // When
        val result = repository.loadSession()
        // Then
        assertThat(result.isExpired()).isFalse()

    }
}