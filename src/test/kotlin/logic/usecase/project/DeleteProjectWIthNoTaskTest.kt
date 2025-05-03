package logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DeleteProjectWIthNoTaskTest {
    @Test
    fun `delete project with no tasks`() {
        val repo = FakeProjectRepository()
        val taskRepo = FakeTaskRepository()
        val useCase = DeleteProjectUseCase(repo, taskRepo)
        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)

        val project = ProjectEntity(UUID.randomUUID(), "Project", admin.id.toString())
        repo.createProject(project)

        useCase(project.id, admin)

        assertNull(repo.getProjectById(project.id.toString()))
    }


}