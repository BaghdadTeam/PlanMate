package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetTasksByProjectIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }

    @Test
    fun `should return tasks for given project id`() {
        val projectId = "project-123"
        val expectedTasks = TaskEntityTestData.tasksInSameProject

        every { taskRepository.getTasksByProjectId(projectId) } returns expectedTasks

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEqualTo(expectedTasks)
        verify { taskRepository.getTasksByProjectId(projectId) }
    }

    @Test
    fun `should return empty list when no tasks found for project id`() {
        val projectId = "project-empty"
        every { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEmpty()
        verify { taskRepository.getTasksByProjectId(projectId) }
    }
}
