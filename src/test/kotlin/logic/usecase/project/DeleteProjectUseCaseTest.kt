package logic.usecase.project

import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class DeleteProjectUseCaseTest {
    lateinit var projectRepository: ProjectRepository
    lateinit var userRepository: UserRepository
    lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should delete project when call DeleteProjectUseCase`() {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper()
        every { userRepository.getUserById(user.id) } returns user


        // When
        deleteProjectUseCase.invoke(project.id, user.id)

        // Then
        verify { projectRepository.deleteProject(project.id) }
    }

    @Test
    fun `should throw AccessDeniedException when user is not admin`() {
        // Given
        val project = ProjectEntity(name = "aboud", creatorId = UUID.randomUUID())
        val user = createUserHelper().copy(type = UserType.Mate)
        every { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<AccessDeniedException> { deleteProjectUseCase.invoke(project.id, user.id) }
    }
}


