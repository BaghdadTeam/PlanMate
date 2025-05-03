package presentation.app

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.presentation.app.StartApp
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class StartAppTest {

    private lateinit var viewer: Viewer
    private lateinit var loginUi: LoginUi
    private lateinit var sessionManager: SessionManager
    private lateinit var startApp: StartApp

    private val dummySession = SessionEntity(
        userId = UUID.randomUUID().toString(),
        token = "dummy-token",
        loginTime = LocalDateTime.now().minusMinutes(5)
    )

    @BeforeEach
    fun setUp() {
        loginUi = mockk()
        sessionManager = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        startApp = StartApp(loginUi, sessionManager, viewer)
    }

    @Test
    fun `should set session when getActiveSession succeeds`() {
        every { sessionManager.getActiveSession() } returns Result.success(dummySession)

        startApp.run()

        verify { sessionManager.clearExpiredSession() }
        verify { sessionManager.getActiveSession() }
        verify { sessionManager.setSession(dummySession) }
        verify(exactly = 0) { loginUi.execute() }
        verify(exactly = 0) { viewer.logMessage(any()) }
        verify(exactly = 0) { viewer.logError(any()) }
    }

    @Test
    fun `should fallback to loginUi when getActiveSession fails`() {
        val exception = Exception("session error")
        val newSession = dummySession.copy(id = UUID.randomUUID())
        every { sessionManager.getActiveSession() } returns Result.failure(exception)
        every { loginUi.execute() } returns newSession

        startApp.run()

        verify { sessionManager.clearExpiredSession() }
        verify { sessionManager.getActiveSession() }
        verify { viewer.logMessage("Failed to load session: session error") }
        verify { loginUi.execute() }
        verify { sessionManager.setSession(newSession) }
    }
}