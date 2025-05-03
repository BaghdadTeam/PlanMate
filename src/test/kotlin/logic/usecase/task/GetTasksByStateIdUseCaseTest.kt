package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetTasksByStateIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByStateIdUseCase: GetTasksByStateIdUseCase

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTasksByStateIdUseCase = GetTasksByStateIdUseCase(taskRepository)
    }

    @Test
    fun `should return tasks for given state id`() {
        val stateId = "state-xyz"
        val expectedTasks = TaskEntityTestData.tasksInSameState

        every { taskRepository.getTasksByStateId(stateId) } returns expectedTasks

        val result = getTasksByStateIdUseCase(stateId)

        assertThat(result).isEqualTo(expectedTasks)
        verify { taskRepository.getTasksByStateId(stateId) }
    }

    @Test
    fun `should return empty list when no tasks match state id`() {
        val stateId = "unknown-state"
        every { taskRepository.getTasksByStateId(stateId) } returns emptyList()

        val result = getTasksByStateIdUseCase(stateId)

        assertThat(result).isEmpty()
        verify { taskRepository.getTasksByStateId(stateId) }
    }
}