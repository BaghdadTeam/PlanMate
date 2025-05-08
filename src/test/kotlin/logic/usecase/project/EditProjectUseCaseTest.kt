package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class EditProjectUseCaseTest {
    lateinit var projectRepository: ProjectRepository
    lateinit var userRepository: UserRepository
    lateinit var editProjectUseCase: EditProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should edit project when call EditProjectUseCase`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Admin)

        every { userRepository.getUserById(user.id) } returns user
        coEvery { projectRepository.getProjectById(project.id) } returns project

        coEvery { projectRepository.editProject(project) } just runs

        // When
        editProjectUseCase.invoke(project.id, project.name, user.id)

        // Then
        coVerify { projectRepository.editProject(project) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not admin`() = runTest {
        // Given
        val projectName = "Test Project"
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Mate)
        every { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<AccessDeniedException> {
            editProjectUseCase.invoke(
                projectId = project.id,
                projectName,
                user.id
            )
        }
    }

    @Test
    fun `should throw EmptyProjectNameException when project name is empty`() = runTest {
        // Given
        val projectName = ""
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper()
        every { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<EmptyProjectNameException> {
            editProjectUseCase.invoke(
                projectId = project.id,
                projectName,
                user.id
            )
        }
    }
}
