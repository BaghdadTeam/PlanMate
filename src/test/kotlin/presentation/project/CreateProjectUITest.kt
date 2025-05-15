package presentation.project

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.CreateProjectUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CreateProjectManagementUITest {
    private lateinit var createProjectUi: CreateProjectUi
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session: SessionManager

    @BeforeEach
    fun setUp() {
        viewer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        session = mockk(relaxed = true)
        createProjectUseCase = mockk()
        createProjectUi = CreateProjectUi(createProjectUseCase, session, viewer, reader)

        every { viewer.logMessage(any()) } just Runs
        every { viewer.logError(any()) } just Runs
    }


    @Test
    fun `should create a project`() = runBlocking {
        // Given
        val projectName = "Test Project"
        val userId = UUID.randomUUID()

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { reader.readInput() } returns projectName
        coEvery { createProjectUseCase(projectName, userId) } returns Unit

        // Act
        createProjectUi.createProject()

        // Then
        verify { viewer.logMessage("=== Create Project ===") }
        verify { viewer.log("Enter project name: ") }
        coVerify { createProjectUseCase(projectName, userId) }
    }

    @Test
    fun `should log error when project name is null`() = runBlocking {
        // Given
        val userId = UUID.randomUUID()

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { reader.readInput() } returns null  // Simulating null input

        // Act
        createProjectUi.createProject()

        // Then
        verify { viewer.logMessage("=== Create Project ===") }
        verify { viewer.log("Enter project name: ") }
        verify { viewer.logError("Project Id should be a number") }
        coVerify(exactly = 0) { createProjectUseCase(any(), any()) }  // Ensure the use case is not called
    }
}