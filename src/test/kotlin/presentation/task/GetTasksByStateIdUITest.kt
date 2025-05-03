package presentation.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTasksByStateIdUI
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
import kotlin.test.Test

class GetTasksByStateIdUITest {

    private lateinit var getTasksByStateIdUI: GetTasksByStateIdUI
    private lateinit var getTasksByStateIdUseCase: GetTasksByStateIdUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    private val tasks = listOf(
        TaskEntity(UUID.randomUUID(), "Task 1", "Description 1", "state1", "project1", "creator1"),
        TaskEntity(UUID.randomUUID(), "Task 2", "Description 2", "state1", "project1", "creator2")
    )

    private val stateId = "state123"
    private val emptyStateId = ""

    @BeforeEach
    fun setup() {
        getTasksByStateIdUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()

        getTasksByStateIdUI = GetTasksByStateIdUI(getTasksByStateIdUseCase, viewer, reader)
    }

    @Test
    fun `test valid state ID input with tasks found`() {
        every { reader.readInput() } returns stateId // User enters valid state ID
        every { getTasksByStateIdUseCase(stateId) } returns tasks

        getTasksByStateIdUI.execute()

        verify { viewer.logMessage("Tasks for state $stateId:") }
        tasks.forEachIndexed { index, task ->
            verify { viewer.logMessage("${index + 1}. ${task.title} - ${task.description}") }
        }
    }

    @Test
    fun `test valid state ID input with no tasks found`() {
        every { reader.readInput() } returns stateId // User enters valid state ID
        every { getTasksByStateIdUseCase(stateId) } returns emptyList()

        getTasksByStateIdUI.execute()

        verify { viewer.logMessage("No tasks found for the given state.") }
    }

    @Test
    fun `test empty state ID input`() {
        every { reader.readInput() } returns emptyStateId // User enters empty state ID

        getTasksByStateIdUI.execute()

        verify { viewer.logMessage("State ID cannot be empty.") }
        verify(exactly = 0) { getTasksByStateIdUseCase(any()) } // Ensure use case is not called
    }

    @Test
    fun `test exception handling when tasks not found`() {
        every { reader.readInput() } returns stateId // User enters valid state ID
        every { getTasksByStateIdUseCase(stateId) } throws TasksNotFoundException("No tasks found")

        getTasksByStateIdUI.execute()

        verify { viewer.logMessage("No tasks found for state ID: $stateId") }
    }

    @Test
    fun `test exception handling for other errors`() {
        every { reader.readInput() } returns stateId // User enters valid state ID
        every { getTasksByStateIdUseCase(stateId) } throws Exception("Unexpected error")

        getTasksByStateIdUI.execute()

        verify { viewer.logMessage("Failed to get tasks: Unexpected error") }
    }
}
