package presentation.task

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.WritingFileException
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.UpdateTaskUI
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class UpdateTaskUITest {

    private lateinit var useCase: UpdateTaskUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var updateTaskUI: UpdateTaskUI

    private val projectID = UUID.randomUUID()

    private val dummyTasks = listOf(
        TaskEntity(
            title = "Old Title",
            description = "Old Description",
            stateId = UUID.randomUUID(),
            projectId = projectID,
            creatorId = UUID.randomUUID()
        ),
        TaskEntity(
            title = "Another Task",
            description = "Some Desc",
            stateId = UUID.randomUUID(),
            projectId = projectID,
            creatorId = UUID.randomUUID()
        )
    )

    private val dummySession = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        token = "dummy-token",
        loginTime = LocalDateTime.now().minusMinutes(10)
    )

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        sessionManager = mockk {
            every { currentSession } returns dummySession
        }
        viewer = mockk(relaxed = true)
        reader = mockk()
        updateTaskUI = UpdateTaskUI(useCase, sessionManager, viewer, reader)
    }

    @Test
    fun `test invalid task number`() {
        every { reader.readInput() } returns "5"
        updateTaskUI.execute(dummyTasks)
        verify { viewer.logMessage("Invalid task number.") }
        confirmVerified(useCase)
    }

    @Test
    fun `entering null for task number`() {
        every { reader.readInput() } returns null
        updateTaskUI.execute(dummyTasks)
        verify { viewer.logMessage("Invalid task number.") }
        confirmVerified(useCase)
    }

    @Test
    fun `test valid task update success`() {
        every { reader.readInput() } returnsMany listOf("1", "New Title", "New Description")

        updateTaskUI.execute(dummyTasks)

        val expectedTask = dummyTasks[0].copy(title = "New Title", description = "New Description")

        coVerify { useCase(expectedTask, dummySession.userId) }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test empty title then valid input`() {
        every { reader.readInput() } returnsMany listOf("1", "", "Valid Title", "Valid Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test empty description then valid input`() {
        every { reader.readInput() } returnsMany listOf("1", "New Title", "", "New Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task description cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test TaskWithMissingTitleException is retried`() {
        every { reader.readInput() } returnsMany listOf("1", "", "Fixed Title", "Valid")

        coEvery { useCase(any(), dummySession.userId) } throws TaskWithMissingTitleException() andThen Unit

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task title is missing.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test TaskWithMissingDescriptionException is retried`() {
        every { reader.readInput() } returnsMany listOf("1", "Valid", "", "Fixed Description")

        coEvery { useCase(any(), dummySession.userId) } throws TaskWithMissingDescriptionException() andThen Unit

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task description is missing.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test TasksNotFoundException is handled`() {
        every { reader.readInput() } returnsMany listOf("1", "Title", "Desc")
        coEvery { useCase(any(), dummySession.userId) } throws TasksNotFoundException()

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task not found.") }
    }

    @Test
    fun `test CsvWriteException is handled`() {
        every { reader.readInput() } returnsMany listOf("1", "Title", "Desc")
        coEvery { useCase(any(), dummySession.userId) } throws WritingFileException()

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Error: Failed to write task to CSV.") }
    }

    @Test
    fun `test unknown exception is handled`() {
        every { reader.readInput() } returnsMany listOf("1", "Title", "Desc")
        coEvery { useCase(any(), dummySession.userId) } throws RuntimeException("Unexpected Error")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Failed to update task: Unexpected Error") }
    }

    @Test
    fun `test expired session returns true`() {
        val expiredSession = dummySession.copy(loginTime = LocalDateTime.now().minusMinutes(31))
        assertThat(expiredSession.isExpired()).isTrue()
    }

    @Test
    fun `test non-numeric input for task index`() {
        every { reader.readInput() } returns "abc"
        updateTaskUI.execute(dummyTasks)
        verify { viewer.logMessage("Invalid task number.") }
        confirmVerified(useCase)
    }

    @Test
    fun `test title with only whitespace then valid input`() {
        every { reader.readInput() } returnsMany listOf("1", "   ", "Valid Title", "Valid Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test description with null then valid input`() {
        every { reader.readInput() } returnsMany listOf("1", "Valid Title", null, "Fixed Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task description cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test task index out of bounds`() {
        every { reader.readInput() } returns "100" // index = 100, index-1 = 99 -> out of bounds
        updateTaskUI.execute(dummyTasks)
        verify { viewer.logMessage("Invalid task number.") }
        confirmVerified(useCase)
    }

    @Test
    fun `test title input is null`() {
        every { reader.readInput() } returnsMany listOf("1", null, "Valid Title", "Valid Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test description input is blank`() {
        every { reader.readInput() } returnsMany listOf("1", "Valid Title", "   ", "Fixed Description")

        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Task description cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task updated successfully.") }
    }

    @Test
    fun `test invalid task index (out of bounds)`() {
        every { reader.readInput() } returns "100"
        updateTaskUI.execute(dummyTasks)

        verify { viewer.logMessage("Invalid task number.") }
        confirmVerified(useCase)
    }
}
