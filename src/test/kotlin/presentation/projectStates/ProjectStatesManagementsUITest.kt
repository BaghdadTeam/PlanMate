package presentation.projectStates

import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifySequence
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.AddTaskStateToProjectUI
import org.baghdad.presentation.projectStates.DeleteStateForProjectUI
import org.baghdad.presentation.projectStates.EditProjectStateUI
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
import org.baghdad.presentation.projectStates.ProjectStatesUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID


class ProjectStatesManagementsUITest {
    private lateinit var addStateToProjectUI: AddTaskStateToProjectUI
    private lateinit var deleteStateForProjectUI: DeleteStateForProjectUI
    private lateinit var editProjectStateUI: EditProjectStateUI
    private lateinit var getAllStatesPerProjectUI: GetAllStatesPerProjectUI
    private lateinit var projectStatesUI: ProjectStatesUI
    private lateinit var reader: Reader
    private lateinit var viewer: Viewer

    @BeforeEach
    fun setUp() {
        reader = mockk()
        viewer = mockk()
        addStateToProjectUI = mockk(relaxed = true)
        deleteStateForProjectUI = mockk()
        editProjectStateUI = mockk()
        getAllStatesPerProjectUI = mockk()

        projectStatesUI = ProjectStatesUI(
            addStateToProjectUI,
            deleteStateForProjectUI,
            editProjectStateUI,
            getAllStatesPerProjectUI,
            reader,
            viewer
        )

        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
        every { viewer.log(any()) } just Runs

    }

    @Test
    fun `should display project states menu and handle user input`() {
        // Given
        val projectId = mockk<UUID>()
        every { reader.readInput() } returns "0"
        every { addStateToProjectUI.execute(projectId) } just Runs

        // When
        projectStatesUI.invoke(projectId)

        // Then
        verifySequence {
            viewer.logMessage(" === Project States === ")
            viewer.logMessage("1. Add State to Project")
            viewer.logMessage("2. Delete State for Project")
            viewer.logMessage("3. Edit Project State")
            viewer.logMessage("0. Back to Previous Screen")
            viewer.logMessage("Enter your choice: ")
            reader.readInput()
        }

    }

    @Test
    fun `should handle invalid inputs and display error message`() {
        // Given
        val projectId = mockk<UUID>()
        every { reader.readInput() } returns "invalid" andThen null andThen "0"

        // When
        projectStatesUI.invoke(projectId)

        // Then
        coVerify(exactly = 2) {
            viewer.logError("Invalid choice. Please try again.")
        }

    }

    @Test
    fun `should add state to project when user chooses 1 to add state`() {
        // Given
        val projectId = UUID.randomUUID()
        every { reader.readInput() } returns "1" andThen "0"
        every { addStateToProjectUI.execute(projectId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify {
            addStateToProjectUI.execute(projectId)
        }

    }

    @Test
    fun `should delete state for project when user chooses 2 to delete state and 1 to select the first state`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns "2" andThen "1" andThen "0"
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(stateId , stateId)
        every { deleteStateForProjectUI.execute(stateId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify {
            deleteStateForProjectUI.execute(stateId)
        }

    }

    @Test
    fun `should view error log when user chooses 2 to delete state and choose invalid project number`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns "2" andThen "Invalid" andThen "0"
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(stateId)
        every { deleteStateForProjectUI.execute(stateId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify { viewer.logError("Invalid choice. Please try again.") }
        coVerify(exactly = 0) {
            deleteStateForProjectUI.execute(stateId)
        }

    }

    @Test
    fun `should edit project state when user chooses 3 to edit state and 1 to select the first state`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns "3" andThen "1" andThen "0"
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(stateId)
        every { editProjectStateUI.execute(stateId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify {
            editProjectStateUI.execute(stateId)
        }

    }

    @Test
    fun `should view error log when user chooses 3 to edit state and choose invalid project number`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns "3" andThen null andThen "0"
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(stateId)
        every { editProjectStateUI.execute(stateId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify { viewer.logError("Invalid choice. Please try again.") }
        coVerify(exactly = 0) {
            editProjectStateUI.execute(stateId)
        }
    }

    @Test
    fun `should view error log when user chooses 3 to edit state and choose project number out of range`() {
        // Given
        val projectId = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        every { reader.readInput() } returns "3" andThen "12" andThen "0"
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(stateId)
        every { editProjectStateUI.execute(stateId) } just Runs

        // When
        projectStatesUI(projectId)

        // Then
        coVerify { viewer.logError("Invalid choice. Please try again.") }
        coVerify(exactly = 0) {
            editProjectStateUI.execute(stateId)
        }
    }
}


