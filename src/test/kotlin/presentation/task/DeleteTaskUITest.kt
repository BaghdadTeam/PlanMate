package presentation.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.DeleteTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.DeleteTaskUI
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class DeleteTaskUITest {

    private lateinit var deleteTaskUI: DeleteTaskUI
    private lateinit var useCase: DeleteTaskUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader

    private val dummySession = SessionEntity(UUID.randomUUID(), UUID.randomUUID().toString(), "", LocalDateTime.now())
    private val task1 = TaskEntity(UUID.randomUUID(), "Task 1", "Description 1", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
    private val task2 = TaskEntity(UUID.randomUUID(), "Task 2", "Description 2", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
    private val tasks = listOf(task1, task2)

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk { every { currentSession } returns dummySession }
        viewer = mockk(relaxed = true)
        reader = mockk()

        deleteTaskUI = DeleteTaskUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `test successful task deletion`() {
        every { reader.readInput() } returns "1" // User selects task 1
        every { sessionManager.currentSession } returns dummySession

        deleteTaskUI.execute(tasks)

        verify { useCase(task1.id, UUID.fromString(dummySession.userId)) }
        verify { viewer.logMessage("Task deleted successfully.") }
    }

    @Test
    fun `test invalid task number`() {
        every { reader.readInput() } returns "3" // User selects invalid task number (out of bounds)

        deleteTaskUI.execute(tasks)

        verify { viewer.logMessage("Invalid task number.") }
        verify(exactly = 0) { useCase(any(), any()) }
    }

    @Test
    fun `test task not found exception`() {
        every { reader.readInput() } returns "1" // User selects task 1
        every { sessionManager.currentSession } returns dummySession
        every { useCase(any(), any()) } throws TasksNotFoundException("Task not found")

        deleteTaskUI.execute(tasks)

        verify { viewer.logMessage("Task not found.") }
    }

    @Test
    fun `test other exceptions during task deletion`() {
        every { reader.readInput() } returns "1" // User selects task 1
        every { sessionManager.currentSession } returns dummySession
        every { useCase(any(), any()) } throws Exception("Unexpected error")

        deleteTaskUI.execute(tasks)

        verify { viewer.logMessage("Failed to delete task: Unexpected error") }
    }

    @Test
    fun `test prompt for task index when input is not a number`() {
        every { reader.readInput() } returns "abc" // User enters a non-numeric value

        deleteTaskUI.execute(tasks)

        verify { viewer.logMessage("Invalid task number.") }
        verify(exactly = 0) { useCase(any(), any()) }
    }
}