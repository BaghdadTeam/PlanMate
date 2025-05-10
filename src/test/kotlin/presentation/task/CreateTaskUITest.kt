package presentation.task

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.GetAllStatesPerProjectUI
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
    private val getAllStatesPerProjectUI: GetAllStatesPerProjectUI = mockk(relaxed = true)

    private val dummySession = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        token = "dummy-token",
        loginTime = LocalDateTime.now().minusMinutes(10)
    )

    private val projectId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        createTaskUseCase = mockk(relaxed = true)
        sessionManager = mockk { every { currentSession } returns dummySession }
        viewer = mockk(relaxed = true)
        reader = mockk()
        createTaskUI = CreateTaskUI(createTaskUseCase, getAllStatesPerProjectUI, sessionManager, viewer, reader)
    }

    @Test
    fun `test successful task creation`() {
        // Given
        val validStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("New Task", "Task Description")
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId)

        // When
        createTaskUI.execute(projectId)

        // Then
        coVerify { createTaskUseCase(any(), any()) }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test empty title then valid title`() {
        // Given
        val validStateId = UUID.randomUUID() // Mock a valid state ID
        every { reader.readInput() } returnsMany listOf("", "Valid Title", "Valid Description") // Mock empty and valid inputs
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId) // Mock non-empty states

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test task title is missing exception`() {
        // Given
        val validStateId = UUID.randomUUID() // Mock a valid state ID
        every { reader.readInput() } returnsMany listOf("", "Valid Description") // Mock empty title
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId) // Mock non-empty states

        coEvery {
            createTaskUseCase(any(), any())
        } throws TaskWithMissingTitleException("Title is missing") andThen Unit

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("Task title is missing.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test task description is missing exception`() {
        // Given
        val validStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf(
            "New Task Title",
            "",
            "New Task Description"
        )
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId)

        coEvery {
            createTaskUseCase(any(), any())
        } throws TaskWithMissingDescriptionException("Description is missing") andThen Unit

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("Task description is missing.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test task creation failure`() {
        // Given
        every { reader.readInput() } returnsMany listOf("New Task Title", "New Task Description")

        coEvery {
            createTaskUseCase(any(), any())
        } throws RuntimeException("Unexpected error")

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("No states found for project $projectId") }
    }

    @Test
    fun `test user session expired returns false`() {
        val expiredSession = dummySession.copy(loginTime = LocalDateTime.now().minusMinutes(31))
        assertThat(expiredSession.isExpired()).isTrue()
    }

    @Test
    fun `test empty title input is retried until valid`() {
        // Given
        val validStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("", "", "Valid Title", "Valid Description")
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId)

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("Task title cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }

    @Test
    fun `test empty description input is retried until valid`() {
        // Given
        val validStateId = UUID.randomUUID()
        every { reader.readInput() } returnsMany listOf("Valid Title", "", "", "Valid Description")
        every { getAllStatesPerProjectUI.execute(projectId) } returns listOf(validStateId)

        // When
        createTaskUI.execute(projectId)

        // Then
        verify { viewer.logError("Task description cannot be empty. Please try again.") }
        verify { viewer.logMessage("Task created successfully.") }
    }
}