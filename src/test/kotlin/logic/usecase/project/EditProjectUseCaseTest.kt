package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var auditRepository: AuditRepository
    private lateinit var adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        adminPermissionCheckerUseCase = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(
            projectRepository,
            auditRepository,
            adminPermissionCheckerUseCase,
            sessionManager
        )
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            editProjectUseCase.invoke(
                UUID.randomUUID(),
                "project",
                UUID.randomUUID()
            )
        }
    }

    @Test
    fun `should edit project when call EditProjectUseCase`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Admin)

        coEvery { adminPermissionCheckerUseCase(user.id) } returns true
        coEvery { projectRepository.getProjectById(project.id) } returns project

        coEvery { projectRepository.editProject(project) } just runs

        // When
        editProjectUseCase.invoke(project.id, project.name, user.id)

        // Then
        coVerify { projectRepository.editProject(project) }
    }

    @Test
    fun `should throw AccessDeniedException when adminPermissionCheckerUseCase return false`() =
        runTest {
            // Given
            val projectName = "Test Project"
            val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
            val user = createUserHelper().copy(type = UserType.Mate)
            coEvery { adminPermissionCheckerUseCase(user.id) } returns false

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
        coEvery { adminPermissionCheckerUseCase(user.id) } returns true

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
