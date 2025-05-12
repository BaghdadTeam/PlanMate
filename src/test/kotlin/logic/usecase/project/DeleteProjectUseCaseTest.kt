package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.enums.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class DeleteProjectUseCaseTest {
    lateinit var projectRepository: ProjectRepository
    lateinit var userRepository: UserRepository
    lateinit var deleteProjectUseCase: DeleteProjectUseCase
    lateinit var auditRepository: AuditRepository

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        auditRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository, userRepository , auditRepository)
    }

    @Test
    fun `should delete project when call DeleteProjectUseCase`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user


        // When
        deleteProjectUseCase.invoke(project.id, user.id)

        // Then
        coVerify { projectRepository.deleteProject(project.id) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not admin`() = runTest {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Mate)
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<AccessDeniedException> { deleteProjectUseCase.invoke(project.id, user.id) }
    }
}


