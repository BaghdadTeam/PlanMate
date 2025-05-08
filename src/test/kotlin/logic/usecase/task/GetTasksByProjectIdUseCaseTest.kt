package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import java.util.*
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
    fun `should return tasks for given project id`() = runTest {

        val expectedTasks = TaskEntityTestData.tasksInSameProject
        val projectId = expectedTasks[0].projectId
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns expectedTasks

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEqualTo(expectedTasks)
        coVerify { taskRepository.getTasksByProjectId(projectId) }
    }

    @Test
    fun `should return empty list when no tasks found for project id`() = runTest {
        val projectId = UUID.randomUUID()
        coEvery { taskRepository.getTasksByProjectId(projectId) } returns emptyList()

        val result = getTasksByProjectIdUseCase(projectId)

        assertThat(result).isEmpty()
        coVerify { taskRepository.getTasksByProjectId(projectId) }
    }
}
