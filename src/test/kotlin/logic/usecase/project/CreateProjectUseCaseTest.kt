package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
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

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        userRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should return True when project is created with valid name and creator`() {
        // Given
        val projectName = "Test Project"
        val user = createUserHelper()

        every { userRepository.getUserById(user.id) } returns user
        every { projectRepository.createProject(any()) } just runs

        // When
        createProjectUseCase(projectName, user.id)

        // Then
        verify { projectRepository.createProject(any()) }
    }

    @Test
    fun `should throw EmptyProjectNameException when project name is empty`() {
        // Given
        val projectName = ""
        val user = createUserHelper()
        every { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<EmptyProjectNameException> { createProjectUseCase(projectName, user.id) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not admin`() {
        // Given
        val projectName = "Test Project"
        val user = createUserHelper().copy(type = UserType.Mate)
        every { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<AccessDeniedException> { createProjectUseCase(projectName, user.id) }
    }
}
