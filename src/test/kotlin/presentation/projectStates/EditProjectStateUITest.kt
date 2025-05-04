package presentation.projectStates

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.junit.jupiter.api.BeforeEach
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

        every { session.userId } returns userId.toString()
        every { sessionManager.currentSession } returns session

        ui = EditProjectStateUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `execute should call useCase with correct stateId and newState`() {
        val stateId = "state-001"
        val name = "In Review"
        val projectId = UUID.randomUUID()

        every { reader.readInput() } returnsMany listOf(stateId, name, projectId.toString())

        ui.execute(projectId)

        verify {
            useCase.invoke(any(), any(), any())
            viewer.logMessage("State updated successfully.")
        }
    }

    @Test
    fun `promptForStateId should retry until non-blank is given`() {
        every { reader.readInput() } returnsMany listOf("", " ", "valid-id")

        val method = ui.javaClass.getDeclaredMethod("promptForStateId")
        method.isAccessible = true
        val result = method.invoke(ui) as String

        assertThat(result).isEqualTo("valid-id")
        verify(exactly = 2) {
            viewer.logError("State ID cannot be blank. Please try again.")
        }
    }

    @Test
    fun `tryEditState should show error message on failure`() {
        val method = ui.javaClass.getDeclaredMethod(
            "tryEditState", String::class.java, StateEntity::class.java, UUID::class.java
        )
        method.isAccessible = true

        val state = StateEntity(name = "To Do", projectId = UUID.randomUUID(), creatorId = userId)
        every { useCase.invoke(any(), any(), any()) } throws Exception("state not found")

        method.invoke(ui, "state-xyz", state, userId)

        verify { viewer.logError("Failed to update state: state not found") }
    }

    @Test
    fun `promptForStateName should log error for blank input and succeed on valid input`() {
        every { reader.readInput() } returnsMany listOf("", "   ", "Valid Name")

        val method = ui.javaClass.getDeclaredMethod("promptForStateName")
        method.isAccessible = true

        val result = method.invoke(ui) as String

        // Verify that the correct error message was logged for the first two invalid inputs
        verify(exactly = 2) {
            viewer.logError("State name cannot be blank. Please try again.")
        }

        // Verify the correct value was returned
        assertThat(result).isEqualTo("Valid Name")
    }

}
