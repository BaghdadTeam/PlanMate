package presentation.task

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.CreateTaskUI
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class CreateTaskUITest {

    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var sessionManager: SessionManager
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var createTaskUI: CreateTaskUI

    private val dummySession = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        token = "dummy-token",
        loginTime = LocalDateTime.now().minusMinutes(10)
    )

    private val projectId = "project123"
    private val stateId = "state123"

    @BeforeEach
    fun setUp() {
        createTaskUseCase = mockk(relaxed = true)
        sessionManager = mockk { every { currentSession } returns dummySession }
        viewer = mockk(relaxed = true)
        reader = mockk()
        createTaskUI = CreateTaskUI(createTaskUseCase, sessionManager, viewer, reader)
    }

    @Test
    fun `test successful task creation`() {
        every { reader.readInput() } returnsMany listOf("New Task", "Task Description")

        createTaskUI.execute(projectId, stateId)

        verify { createTaskUseCase(any(), any()) }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test empty title then valid title`() {
        every { reader.readInput() } returnsMany listOf("", "Valid Title", "Valid Description")

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test task title is missing exception`() {
        every { reader.readInput() } returnsMany listOf("", "New Task Title", "New Task Description")

        every {
            createTaskUseCase(any(), any())
        } throws TaskWithMissingTitleException("Title is missing") andThen Unit

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Task title is missing.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test task description is missing exception`() {
        every { reader.readInput() } returnsMany listOf("New Task Title", "", "New Task Description")

        every {
            createTaskUseCase(any(), any())
        } throws TaskWithMissingDescriptionException("Description is missing") andThen Unit

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Task description is missing.") }
        verify { viewer.logMessage("Task created successfully.") }
    }


    @Test
    fun `test task creation failure`() {
        every { reader.readInput() } returnsMany listOf("New Task Title", "New Task Description")

        every {
            createTaskUseCase(any(), any())
        } throws RuntimeException("Unexpected error")

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Failed to create task: Unexpected error") }
    }

    @Test
    fun `test user session expired returns false`() {
        val expiredSession = dummySession.copy(loginTime = LocalDateTime.now().minusMinutes(31))
        assertThat(expiredSession.isExpired()).isTrue()
    }

    @Test
    fun `test empty title input is retried until valid`() {
        every { reader.readInput() } returnsMany listOf("", "", "Valid Title", "Valid Description")

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test empty description input is retried until valid`() {
        every { reader.readInput() } returnsMany listOf("Valid Title", "", "", "Valid Description")

        createTaskUI.execute(projectId, stateId)

        verify { viewer.logError("Task description cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }
}
