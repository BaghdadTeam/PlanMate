package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
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

        val expectedTasks = TaskEntityTestData.tasksInSameProject
        val projectId = expectedTasks[0].projectId
        every { taskRepository.getTasksByProjectId(projectId) } returns expectedTasks

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEqualTo(expectedTasks)
        verify { taskRepository.getTasksByProjectId(projectId) }
    }

    @Test
    fun `should return empty list when no tasks found for project id`() {
        val projectId = UUID.randomUUID()
        every { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEmpty()
        verify { taskRepository.getTasksByProjectId(projectId) }
    }
}
