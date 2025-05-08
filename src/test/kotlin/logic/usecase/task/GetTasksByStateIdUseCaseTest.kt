package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.junit.jupiter.api.BeforeEach
import java.util.*
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
    fun `should return tasks for given state id`() = runTest {

        val expectedTasks = TaskEntityTestData.tasksInSameState
        val stateId = expectedTasks[0].stateId
        coEvery { taskRepository.getTasksByStateId(stateId) } returns expectedTasks

        val result = getTasksByStateIdUseCase(stateId)

        assertThat(result).isEqualTo(expectedTasks)
        coVerify { taskRepository.getTasksByStateId(stateId) }
    }

    @Test
    fun `should return empty list when no tasks match state id`() = runTest {
        val stateId = UUID.randomUUID()
        coEvery { taskRepository.getTasksByStateId(stateId) } returns emptyList()

        val result = getTasksByStateIdUseCase(stateId)

        assertThat(result).isEmpty()
        coVerify { taskRepository.getTasksByStateId(stateId) }
    }
}