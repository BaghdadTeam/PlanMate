package logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import java.util.UUID
import kotlin.getOrThrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteProjectWithTasksTest {

    @Test
    fun `delete project with tasks should throw`() {
        val projectRepo = FakeProjectRepository()
        val taskRepo = FakeTaskRepository()
        val useCase = DeleteProjectUseCase(projectRepo, taskRepo)

        val admin = UserEntity(UUID.randomUUID(), "admin", "pass", "", UserType.Admin)
        val project = ProjectEntity(UUID.randomUUID(), "Demo Project", admin.id.toString())
        projectRepo.createProject(project)

        // Add a task to simulate active tasks
        val task = TaskEntity(UUID.randomUUID(), "Task", project.id.toString(), "TODO", admin.id.toString(), "")
        taskRepo.createTask(task)

        val exception = assertFailsWith<IllegalAccessException> {
            useCase(project.id, admin)
        }

        assertEquals("Can't delete project with active task!.", exception.message)
    }
}

