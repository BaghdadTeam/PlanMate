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

class DeleteProjectUiTest {

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
        val projectIds = listOf(UUID.randomUUID(), UUID.randomUUID())
        val projectId = 1 // Selecting the second project
        val userId = UUID.randomUUID()

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns projectIds
        coEvery { reader.readInput() } returns projectId.toString()
        coEvery { deleteProjectUseCase(projectIds[projectId], userId) } returns Unit

        // Act
        deleteProjectUi.deleteProject()

        // Then
        verify { viewer.logMessage("=== Delete Project ===") }
        verify { viewer.logMessage("=== View Project ===") }
        coVerify { deleteProjectUseCase(projectIds[projectId], userId) }
    }

    @Test
    fun `should log error when project ID is not a number`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { getAllProjectsUi() } returns listOf(UUID.randomUUID(), UUID.randomUUID())
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