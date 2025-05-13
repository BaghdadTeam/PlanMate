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
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTasksByProjectIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private val sessionManager: SessionManager = mockk()

    @BeforeEach
    fun setUp() {
        taskRepository = mockk(relaxed = true)
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository,sessionManager,)
        coEvery { sessionManager.isAuthenticated() } returns true
    }
    @Test
    fun `should throw Unauthorized exception  when user not authenticated `() = runTest {
        coEvery { sessionManager.isAuthenticated() } returns false
        assertThrows<UnauthorizedException> {
            getTasksByProjectIdUseCase.invoke(UUID.randomUUID())
        }
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
