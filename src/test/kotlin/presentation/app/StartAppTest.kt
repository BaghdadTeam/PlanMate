import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.presentation.app.StartApp
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.output.Viewer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StartAppTest {
    lateinit var viewer: Viewer
    lateinit var loginUi: LoginUi
    lateinit var sessionManager: SessionManager
    lateinit var startApp: StartApp

    @BeforeEach
    fun setUp() {
        loginUi = mockk()

        sessionManager = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        startApp = StartApp(
            loginUi, sessionManager, viewer
        )

    }

    @Test
    fun `should at least call clean expired session once`() {
        startApp.run()
        verify { sessionManager.clearExpiredSession()}
    }
    @Test
    fun `should at least get active session`() {
        startApp.run()
        verify { sessionManager.getActiveSession() }
    }

}