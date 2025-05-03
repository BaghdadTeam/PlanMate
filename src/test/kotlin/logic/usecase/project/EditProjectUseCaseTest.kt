package logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.project.EditProjectUseCase
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class EditProjectUseCaseTest {
    @Test
    fun `edit project name`() {
        val repo = FakeProjectRepository()
        val useCase = EditProjectUseCase(repo)
        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)

        val project = ProjectEntity(UUID.randomUUID(), "Initial", admin.id.toString())
        repo.createProject(project)

        useCase(project.id, "Updated Name", admin)

        val updated = repo.getProjectById(project.id.toString())
        assertEquals("Updated Name", updated?.name)
    }

}