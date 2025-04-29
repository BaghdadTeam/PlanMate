package data.local

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.SessionDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.InvalidSessionException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SessionDataSourceTest {
    lateinit var dataSource: DataSource<SessionEntity>
    lateinit var sessionDataSource: SessionDataSource

    @BeforeEach
    fun setUp() {
        dataSource = mockk()
        sessionDataSource = SessionDataSource(dataSource)
    }

    @Test
    fun `should load session data successfully from the database when loadSession is called`() {
        // Given
        every { dataSource.loadAll() } returns listOf(SessionTestData.baseSession)
        // When
        val result = sessionDataSource.loadSession()
        // Then
        assertThat(result?.id).isEqualTo(SessionTestData.baseSession.id)

    }
    @Test
    fun `should delete session data successfully when deleteSession function is called`() {
        // Given
        every { dataSource.loadAll() } returns listOf(SessionTestData.baseSession)
        // When
        val result = sessionDataSource.deleteSession()
        // Then
        assertThat(result).isTrue()
    }
    @Test
    fun `should save session data successfully when saveSession function invoked`() {
        // Given
        every { dataSource.loadAll() } returns listOf()
        // When
        sessionDataSource.saveSession(SessionTestData.baseSession)
        // Then
        assertThat(dataSource.loadAll()).isNotEmpty()

    }
    @Test
    fun `Should return null if there i no session data found`() {
        // Given
        every { dataSource.loadAll() } returns listOf()
        // When
        val result = sessionDataSource.loadSession()
        // Then
        assertThat(result).isNull()
    }
}