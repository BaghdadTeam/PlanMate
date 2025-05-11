package data.local

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.SessionDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SessionDataSourceTest {
    lateinit var dataSource: DataSource<SessionEntity>
    lateinit var sessionDataSource: SessionDataSource

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        sessionDataSource = SessionDataSource(dataSource)
    }

    @Test
    fun `should load session data successfully from the database when loadSession is called`()= runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(SessionTestData.baseSession)
        // When
        val result = sessionDataSource.loadSession()
        // Then
        assertThat(result.id).isEqualTo(SessionTestData.baseSession.id)

    }
    @Test
    fun `should execute dataSource update when deleteSession function is invoked`()= runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf(SessionTestData.baseSession)
        // When
        sessionDataSource.deleteSession()
        // Then
        coVerify { dataSource.delete(any()) }
    }
    @Test
    fun `should execute  dataSource append when saveSession is called`()= runTest {
        // Given
        coEvery { dataSource.loadAll() } returns emptyList()
        // When
        sessionDataSource.saveSession(SessionTestData.baseSession)
        // Then
        coVerify(exactly = 1) { dataSource.append(SessionTestData.baseSession) }

    }
    @Test
    fun `Should return null if there i no session data found`()= runTest {
        // Given
        coEvery { dataSource.loadAll() } returns listOf()
       // When & Then
        assertThrows<SessionNotFoundException> {sessionDataSource.loadSession()  }
    }
}