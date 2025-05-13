package logic.usecase.task

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTasksByStateIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByStateIdUseCase: GetTasksByStateIdUseCase
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTasksByStateIdUseCase = GetTasksByStateIdUseCase(taskRepository,sessionManager,)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            getTasksByStateIdUseCase.invoke(UUID.randomUUID())
        }
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