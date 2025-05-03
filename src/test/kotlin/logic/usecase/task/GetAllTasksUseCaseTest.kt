package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `should return a list of tasks`() {
        // Given
        val expectedTasks = TaskEntityTestData.tasks
        every { taskRepository.getAllTasks() } returns expectedTasks

        // When
        val result = getAllTasksUseCase.invoke()

        // Then
        assertThat(result).isEqualTo(expectedTasks)
        verify { taskRepository.getAllTasks() }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() {
        every { taskRepository.getAllTasks() } returns emptyList()

        val result = getAllTasksUseCase.invoke()

        assertThat(result).isEmpty()
        verify { taskRepository.getAllTasks() }
    }
}