package data.repository.authentication

import helpers.authentication.SessionTestData
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
        sessionDataSource = mockk()
        repository = SessionRepositoryImpl(sessionDataSource)
    }
    @Test
    fun `Should invoke sessionDataSource saveSession when saveSession is called`() {

        repository.saveSession(SessionTestData.baseSession)
        verify { sessionDataSource.saveSession(any()) }
    }
    @Test
    fun `Should invoke sessionDataSource delete when saveSession is called`() {
        repository.saveSession(SessionTestData.baseSession)
    }
}