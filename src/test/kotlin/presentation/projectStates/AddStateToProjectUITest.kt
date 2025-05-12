package presentation.projectStates

import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.AddTaskStateToProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.AddTaskStateToProjectUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test


class AddStateToProjectUITest {

    private lateinit var useCase: AddTaskStateToProjectUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: AddTaskStateToProjectUI

    private val fakeSession = mockk<SessionEntity>(relaxed = true)
    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk()
        viewer = mockk(relaxed = true)
        reader = mockk()
        every { fakeSession.userId } returns userId
        every { sessionManager.currentSession } returns fakeSession

        ui = AddTaskStateToProjectUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `execute should call useCase with correct state`() {
        val stateName = "To Do"
        val projectId = UUID.randomUUID()

        every { reader.readInput() } returnsMany listOf(stateName)

        ui.execute(projectId)

        coVerify {
            useCase.invoke(
                match {
                    it.name == stateName &&
                            it.projectId == projectId &&
                            it.creatorId == userId
                },
                userId
            )
        }

        verify { viewer.logMessage("State 'To Do' added to project successfully.") }
    }


    @Test
    fun `promptForStateName should loop until valid name is provided`() {
        every { reader.readInput() } returnsMany listOf("", "  ", "Done")

        ui.execute(UUID.randomUUID())

        verify(exactly = 2) { viewer.logError("State name cannot be blank. Please try again.") }
    }

    @Test
    fun `tryAddState should show error message when useCase throws exception`() {
        every { reader.readInput() } returnsMany listOf("", "  ", "Done")

        val state = StateEntity(name = "Done", projectId = UUID.randomUUID(), creatorId = userId)

        coEvery { useCase.invoke(any(), any()) } throws RuntimeException("Something went wrong")

        ui.execute(state.projectId)

        verify { viewer.logError("Failed to add state") }
    }
}
