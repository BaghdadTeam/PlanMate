package logic.usecase.project

import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import io.mockk.*
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.common.Result
import java.util.*
import kotlin.test.Test
import kotlin.test.assertTrue

class DeleteProjectWithNoTaskTest {

    @Test
    fun `should return success when project exists and has no tasks`() {
        // Arrange
        val projectRepo = mockk<ProjectRepository>(relaxed = true)
        val taskRepo = mockk<TaskRepository>()
        val useCase = DeleteProjectUseCase(projectRepo, taskRepo)

        val projectId = UUID.randomUUID()
        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)
        val project = ProjectEntity(projectId, "Project", admin.id.toString())

        every { taskRepo.getTasksByProjectId(projectId.toString()) } returns emptyList()
        every { projectRepo.deleteProject(projectId.toString()) } returns true

        // Act
        val result = useCase(projectId, admin)

        // Assert
        assertTrue(result is Result.Success)

        verify(exactly = 1) { taskRepo.getTasksByProjectId(projectId.toString()) }
        verify(exactly = 1) { projectRepo.deleteProject(projectId.toString()) }

        confirmVerified(projectRepo, taskRepo)
    }
}
