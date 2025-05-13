package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class DeleteProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var auditRepository: AuditRepository
    private val sessionManager: SessionManager = mockk()
    private lateinit var adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase


    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        adminPermissionCheckerUseCase = mockk()
        deleteProjectUseCase =
            DeleteProjectUseCase(projectRepository, auditRepository, adminPermissionCheckerUseCase , sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true
    }

    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> { deleteProjectUseCase.invoke(UUID.randomUUID(), UUID.randomUUID()) }
    }

    @Test
    fun `should delete project when call DeleteProjectUseCase`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper()
        coEvery { adminPermissionCheckerUseCase(user.id) } returns true


        // When
        deleteProjectUseCase.invoke(project.id, user.id)

        // Then
        coVerify { projectRepository.deleteProject(project.id) }
    }

    @Test
    fun `should throw AccessDeniedException when adminPermissionCheckerUseCase return false`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Mate)
        coEvery { adminPermissionCheckerUseCase(user.id) } returns false

        // When & Then
        assertThrows<AccessDeniedException> { deleteProjectUseCase.invoke(project.id, user.id) }
    }
}


