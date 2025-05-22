package presentation.projectStates

import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class EditProjectStateUITest {

    private lateinit var useCase: EditProjectStatesUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var ui: EditProjectStateUI
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

        ui = EditProjectStateUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `execute should call useCase with correct stateId and newState`() {
        // Arrange
        val stateId = UUID.randomUUID()
        val name = "In Review"
        val userId = UUID.randomUUID()

        every { reader.readInput() } returns name
        every { sessionManager.currentSession } returns SessionEntity(
            id = UUID.randomUUID(),
            userId = userId,
            token = UUID.randomUUID().toString(),
            loginTime = LocalDateTime.now(),
        )

        // Act
        ui.execute(stateId)

        // Assert
        coVerify {
            useCase.invoke(
                stateId = stateId,
                newTaskStateName = name,
                userId = userId
            )
        }
        verify { viewer.logMessage("State updated successfully.") }
    }

    @Test
    fun `promptForStateId should retry until non-blank is given`() {
        every { reader.readInput() } returnsMany listOf("", " ", "valid-id")

        ui.execute(UUID.randomUUID())

        verify(exactly = 2) {
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }

    @Test
    fun `tryEditState should show error message on failure`() {
        val stateId = UUID.randomUUID()

        every { reader.readInput() } returns "Valid State Name"
        coEvery { useCase.invoke(any(), any(), any()) } throws Exception("state not found")

        ui.execute(stateId)

        verify { viewer.logError("Failed to update state: state not found") }
    }


    @Test
    fun `promptForStateName should log error for blank input and succeed on valid input`() {
        every { reader.readInput() } returnsMany listOf("", "   ", "Valid Name")

        ui.execute(UUID.randomUUID())

        verify(exactly = 2) {
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }
}
