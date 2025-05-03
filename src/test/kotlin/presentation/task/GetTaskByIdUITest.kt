package presentation.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTaskByIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTaskByIdUI
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class GetTaskByIdUITest {

    private lateinit var getTaskByIdUI: GetTaskByIdUI
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    private val task = TaskEntity(UUID.randomUUID(), "Test Task", "Task Description", "state", "project", "creator")
    private val invalidTaskId = "invalid-uuid"

    @BeforeEach
    fun setup() {
        getTaskByIdUseCase = mockk(relaxed = true)
        viewer = mockk(relaxed = true)
        reader = mockk()

        getTaskByIdUI = GetTaskByIdUI(getTaskByIdUseCase, reader, viewer)
    }

    @Test
    fun `test valid task ID input`() {
        val taskId = UUID.randomUUID()
        every { reader.readInput() } returns taskId.toString() // User enters valid task ID
        every { getTaskByIdUseCase(taskId) } returns task

        getTaskByIdUI.execute()

        verify { getTaskByIdUseCase(taskId) }
        verify { viewer.logMessage("Task found:") }
        verify { viewer.logMessage("Title: Test Task") }
        verify { viewer.logMessage("Description: Task Description") }
    }

    @Test
    fun `test invalid task ID format`() {
        every { reader.readInput() } returns invalidTaskId // User enters invalid task ID format

        getTaskByIdUI.execute()

        verify { viewer.logMessage("Invalid task ID format.") }
        verify(exactly = 0) { getTaskByIdUseCase(any()) } // Ensure use case is not called
    }

    @Test
    fun `test task not found exception`() {
        val taskId = UUID.randomUUID()
        every { reader.readInput() } returns taskId.toString() // User enters valid task ID
        every { getTaskByIdUseCase(taskId) } throws TasksNotFoundException("No task found")

        getTaskByIdUI.execute()

        verify { viewer.logMessage("No task found with ID: $taskId") }
    }

    @Test
    fun `test other exceptions during task retrieval`() {
        val taskId = UUID.randomUUID()
        every { reader.readInput() } returns taskId.toString() // User enters valid task ID
        every { getTaskByIdUseCase(taskId) } throws Exception("Unexpected error")

        getTaskByIdUI.execute()

        verify { viewer.logMessage("Failed to get task: Unexpected error") }
    }
}