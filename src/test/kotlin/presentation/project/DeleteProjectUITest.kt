package presentation.project

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.DeleteProjectUi
import org.baghdad.presentation.project.GetAllProjectsUi
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class DeleteProjectManagementUITest {

    private lateinit var deleteProjectUi: DeleteProjectUi
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager
    private lateinit var getAllProjectsUi: GetAllProjectsUi

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        session = mockk(relaxed = true)
        deleteProjectUseCase = mockk()
        getAllProjectsUi = mockk()
        deleteProjectUi = DeleteProjectUi(deleteProjectUseCase, session, getAllProjectsUi, viewer, reader)

        // Explicit behavior for log messages
        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
    }

    @Test
    fun `should delete a project`() = runBlocking {
        // Given
        val firstProjectID = UUID.randomUUID()
        val secondProjectID = UUID.randomUUID()
        val projects = listOf(firstProjectID, secondProjectID) to listOf("Project 1", "Project 2")
        val projectId = 1 // The user will input '1' which maps to index 0
        val userId = UUID.randomUUID()

        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns projectId.toString()
        coEvery { deleteProjectUseCase(firstProjectID, userId) } returns Unit

        // Act
        deleteProjectUi.deleteProject()

        // Then
        verify { viewer.logMessage("=== Delete Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        coVerify { deleteProjectUseCase(firstProjectID, userId) }
    }

    @Test
    fun `should log error when project ID is not a number`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()
        val projects = listOf(UUID.randomUUID(), UUID.randomUUID()) to listOf("Project 1", "Project 2")

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projects
        coEvery { reader.readInput() } returns "invalid"  // Simulating non-numeric input

        // Act
        deleteProjectUi.deleteProject()

        // Then
        verify { viewer.logMessage("=== Delete Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        verify { viewer.logError("Project Id should be a number") }
        coVerify(exactly = 0) { deleteProjectUseCase(any(), any()) }  // Ensure the use case is not called
    }
}