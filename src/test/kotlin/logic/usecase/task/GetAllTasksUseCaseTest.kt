package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetAllTasksUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllTasksUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository)
    }

    @Test
    fun `should return a list of tasks`() = runTest {
        // Given
        val expectedTasks = TaskEntityTestData.tasks
        coEvery { taskRepository.getAllTasks() } returns expectedTasks

        // When
        val result = getAllTasksUseCase.invoke()

        // Then
        assertThat(result).isEqualTo(expectedTasks)
        coVerify { taskRepository.getAllTasks() }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runTest {
        coEvery { taskRepository.getAllTasks() } returns emptyList()

        val result = getAllTasksUseCase.invoke()

        assertThat(result).isEmpty()
        coVerify { taskRepository.getAllTasks() }
    }
}