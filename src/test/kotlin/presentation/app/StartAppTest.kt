package presentation.app

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionNotFoundException
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
        userId = UUID.randomUUID(),
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
    fun `should set session when getActiveSession succeeds`() = runTest {
        coEvery { sessionManager.getActiveSession() } returns dummySession

        startApp.run()

        coVerify { sessionManager.clearExpiredSession() }
        coVerify { sessionManager.getActiveSession() }
        coVerify { sessionManager.setSession(dummySession) }
        coVerify(exactly = 0) { loginUi.execute() }
    }

    @Test
    fun `should fallback to loginUi when getActiveSession fails`() {
        val newSession = dummySession.copy(id = UUID.randomUUID())
        coEvery { sessionManager.getActiveSession() } throws SessionNotFoundException("Session not found")
        coEvery { loginUi.execute() } returns newSession

        startApp.run()

        coVerify { sessionManager.clearExpiredSession() }
        coVerify { sessionManager.getActiveSession() }
        coVerify { loginUi.execute() }
        verify { sessionManager.setSession(newSession) }
    }
}