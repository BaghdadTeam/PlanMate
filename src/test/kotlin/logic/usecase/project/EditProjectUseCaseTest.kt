package logic.usecase.project

import io.mockk.mockk
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.project.EditProjectUseCase
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import io.mockk.*
import org.baghdad.logic.usecase.common.Result
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EditProjectUseCaseTest {

    @Test
    fun `should return success when project exists and is edited successfully`() {
        // Arrange
        val projectRepo = mockk<ProjectRepository>()
        val useCase = EditProjectUseCase(projectRepo)
        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)
        val projectId = UUID.randomUUID()
        val existingProject = ProjectEntity(projectId, "Initial", admin.id.toString())

        every { projectRepo.getProjectById(projectId.toString()) } returns existingProject
        every { projectRepo.editProject(any()) } just Runs

        // Act
        val result = useCase(projectId, "Updated Name", admin)

        // Assert
        assertTrue(result is Result.Success)

        val expected = existingProject.copy(name = "Updated Name")
        verify(exactly = 1) { projectRepo.getProjectById(projectId.toString()) }
        verify(exactly = 1) { projectRepo.editProject(expected) }

        confirmVerified(projectRepo)
    }
}
