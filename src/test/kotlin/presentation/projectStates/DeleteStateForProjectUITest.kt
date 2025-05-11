package presentation.projectStates

import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.DeleteStateForProjectUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class DeleteStateForProjectUITest {

    private lateinit var useCase: DeleteStateForProjectUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: DeleteStateForProjectUI
    private val session = mockk<SessionEntity>()
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()

        every { session.userId } returns userId
        every { sessionManager.currentSession } returns session

        ui = DeleteStateForProjectUI(useCase, sessionManager, viewer)
    }

    @Test
    fun `execute should call useCase with correct values`() {
        val stateId = UUID.randomUUID()

        ui.execute(stateId)

        coVerify {
            useCase.invoke(stateId, userId)
        }
        verify { viewer.logMessage("State deleted successfully.") }
    }

    @Test
    fun `execute should log error on invalid state ID`() {
        val stateId = UUID.randomUUID()

        coEvery { useCase.invoke(any(), any()) } throws Exception("State not found")

        ui.execute(stateId)

        verify { viewer.logError("Failed to delete state: State not found") }
    }
    @Test
    fun `tryDeleteState should log error on exception`() {
        val stateId = UUID.randomUUID()

        coEvery { useCase.invoke(any(), any()) } throws Exception("State not found")

        ui.execute(stateId)

        verify { viewer.logError("Failed to delete state: State not found") }
    }
}