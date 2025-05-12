package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.enums.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CreateProjectUseCaseTest {
    lateinit var projectRepository: ProjectRepository
    lateinit var userRepository: UserRepository
    lateinit var createProjectUseCase: CreateProjectUseCase
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        userRepository = mockk()
        auditRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository, userRepository , auditRepository)
    }

    @Test
    fun `should return True when project is created with valid name and creator`() = runTest {
        // Given
        val projectName = "Test Project"
        val user = createUserHelper()

        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { projectRepository.createProject(any()) } just runs
        coEvery { auditRepository.addAuditEntry(any()) } just runs
        // When
        createProjectUseCase(projectName, user.id)

        // Then
        coVerify { projectRepository.createProject(any()) }
    }

    @Test
    fun `should throw EmptyProjectNameException when project name is empty`()  = runTest {
        // Given
        val projectName = ""
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<EmptyProjectNameException> { createProjectUseCase(projectName, user.id) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not admin`()  = runTest {
        // Given
        val projectName = "Test Project"
        val user = createUserHelper().copy(type = UserType.Mate)
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<AccessDeniedException> { createProjectUseCase(projectName, user.id) }
    }
}
