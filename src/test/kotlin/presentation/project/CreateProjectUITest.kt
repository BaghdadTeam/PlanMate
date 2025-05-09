package presentation.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.CreateProjectUi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class CreateProjectUiTest {

    private lateinit var createProjectUi: CreateProjectUi
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var viewer: Viewer
    private lateinit var reader: Reader
    private lateinit var session : SessionManager

    @BeforeEach
    fun setUp() {
        viewer = mockk()
        reader = mockk()
        session = mockk()
        createProjectUseCase = mockk()
        createProjectUi = CreateProjectUi(createProjectUseCase, session ,viewer, reader)
    }

    @Test
    fun `should create a project`() {
        // Given
        val projectName = "Test Project"
        val userId = UUID.randomUUID()

        // when
        coEvery { session.currentSession.userId } returns userId
        coEvery { reader.readInput() } returns projectName
        coEvery { createProjectUseCase(projectName, userId) } returns Unit

        runBlocking { createProjectUi }

        // Then
        verify { viewer.logMessage("=== Create Project ===") }
        verify { viewer.logMessage("Enter project name: ") }
        coVerify {  createProjectUseCase(projectName, userId)  }
    }
}