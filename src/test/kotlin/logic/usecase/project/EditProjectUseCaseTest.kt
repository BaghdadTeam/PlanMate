package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var userRepository: UserRepository
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var auditRepository: AuditRepository
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository, userRepository , auditRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> { editProjectUseCase.invoke(UUID.randomUUID(), "project", UUID.randomUUID()) }
    }

    @Test
    fun `should edit project when call EditProjectUseCase`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Admin)

        coEvery { userRepository.getUserById(user.id) } returns user
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
        coEvery { userRepository.getUserById(user.id) } returns user

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
        coEvery { userRepository.getUserById(user.id) } returns user

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
