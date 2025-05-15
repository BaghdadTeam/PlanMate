package presentation.project

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.EditProjectUi
import org.baghdad.presentation.project.GetAllProjectsUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class EditProjectManagementUITest {

    private lateinit var editProjectUi: EditProjectUi
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager
    private lateinit var getAllProjectsUi: GetAllProjectsUi

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        session = mockk(relaxed = true)
        getAllProjectsUi = mockk()
        editProjectUseCase = mockk()
        editProjectUi = EditProjectUi(editProjectUseCase, session, viewer, getAllProjectsUi, reader)

        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
    }

    @Test
    fun `should edit project successfully`() = runBlocking {
        // Given
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")
        val userId = UUID.randomUUID()
        val projectId = 0
        val newProjectName = "New Project Name"

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns projectId.toString() andThen newProjectName
        coEvery { editProjectUseCase(projects.first[projectId], newProjectName, userId) } returns Unit

        // Act
        editProjectUi.editProject()

        // Then
        verify { viewer.logMessage("=== Edit Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        verify { viewer.logMessage("Enter new project name: ") }
        coVerify { editProjectUseCase(projects.first[projectId], newProjectName, userId) }
    }

    @Test
    fun `should log error when project id is not a number`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns "invalid"

        // Act
        editProjectUi.editProject()

        // Then
        verify { viewer.logMessage("=== Edit Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        verify { viewer.logMessage("Enter new project name: ") }
        verify { viewer.logError("Project Id should be a number") }
        coVerify(exactly = 0) { editProjectUseCase(any(), any(), any()) }  // Ensure use case is not called
    }

    @Test
    fun `should log error when new project name is null`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()
        val projectId = 0
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")


        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns projectId.toString() andThen null  // Simulating null project name input

        // Act
        editProjectUi.editProject()

        // Then
        verify { viewer.logMessage("=== Edit Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        verify { viewer.logMessage("Enter new project name: ") }
        verify { viewer.logError("Project Id should be a number") }  // Adjust error message based on the condition
        coVerify(exactly = 0) { editProjectUseCase(any(), any(), any()) }  // Ensure the use case is not called
    }
}