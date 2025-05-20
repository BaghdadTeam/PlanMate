package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class CreateProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var auditRepository: AuditRepository
    private val sessionManager: SessionManager = mockk()


    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        auditRepository = mockk()
        createProjectUseCase =
            CreateProjectUseCase(projectRepository, auditRepository, sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            createProjectUseCase.invoke(
                "project",
                UUID.randomUUID()
            )
        }
    }

    @Test
    fun `should return True when project is created with valid name and creator`() = runTest {
        // Given
        val projectName = "Test Project"
        val user = createUserHelper()

        coEvery { projectRepository.createProject(any()) } just runs
        coEvery { auditRepository.addAuditEntry(any()) } just runs
        // When
        createProjectUseCase(projectName, user.id)

        // Then
        coVerify { projectRepository.createProject(any()) }
    }

    @Test
    fun `should throw EmptyProjectNameException when project name is empty`() = runTest {
        // Given
        val projectName = ""
        val user = createUserHelper()

        // When & Then
        assertThrows<EmptyProjectNameException> { createProjectUseCase(projectName, user.id) }
    }
}
