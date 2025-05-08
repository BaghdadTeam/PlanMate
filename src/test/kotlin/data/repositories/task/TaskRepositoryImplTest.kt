package data.repositories.task

import com.google.common.truth.Truth
import helpers.task.TaskEntityTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.TaskDataSource
import org.baghdad.data.repositories.task.TaskRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class TaskRepositoryImplTest {

    private lateinit var dataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepositoryImpl

    @BeforeEach
    fun setUp() {
        dataSource = mockk(relaxed = true)
        taskRepository = TaskRepositoryImpl(dataSource)
    }

    @Test
    fun `getAllTasks should return list from dataSource`()= runTest {
        val mockTasks = listOf(
            TaskEntityTestData.normalTask,
            TaskEntityTestData.normalTask.copy(id = UUID.randomUUID(), title = "Task 2"),
        )
        coEvery { dataSource.loadTasks() } returns mockTasks

        val result = taskRepository.getAllTasks()

        Truth.assertThat(result).isEqualTo(mockTasks)
        coVerify { dataSource.loadTasks() }
    }

    @Test
    fun `createTask should call dataSource addTask`()= runTest {
        val task = TaskEntityTestData.normalTask

        taskRepository.createTask(task)

        coVerify { dataSource.addTask(task) }
    }

    @Test
    fun `getTaskById should return correct task`() = runTest {
        val task = TaskEntityTestData.normalTask
        val id = task.id
        coEvery { dataSource.getTaskById(id) } returns task

        val result = taskRepository.getTaskById(id)

        Truth.assertThat(result).isEqualTo(task)
        coVerify { dataSource.getTaskById(id) }
    }

    @Test
    fun `getTasksByProjectId should return filtered tasks`() = runTest {
        val tasks = TaskEntityTestData.tasksInSameProject
        val projectID = UUID.randomUUID()
        coEvery { dataSource.getTasksByProjectId(projectID) } returns tasks

        val result = taskRepository.getTasksByProjectId(projectID)

        Truth.assertThat(result).isEqualTo(tasks)
        coVerify { dataSource.getTasksByProjectId(projectID) }
    }

    @Test
    fun `getTasksByStateId should return filtered tasks`() = runTest {
        val tasks = TaskEntityTestData.tasksInSameState
        val stateID = UUID.randomUUID()
        coEvery { dataSource.getTasksByStateId(stateID) } returns tasks

        val result = taskRepository.getTasksByStateId(stateID)

        Truth.assertThat(result).isEqualTo(tasks)
        coVerify { dataSource.getTasksByStateId(stateID) }
    }

    @Test
    fun `updateTask should call dataSource updateTask`() = runTest {
        val task = TaskEntityTestData.normalTask

        taskRepository.updateTask(task)

        coVerify { dataSource.updateTask(task) }
    }

    @Test
    fun `deleteTask should call dataSource deleteTask`() = runTest {
        val taskId = UUID.randomUUID()

        taskRepository.deleteTask(taskId)

        coVerify { dataSource.deleteTask(taskId) }
    }

    @Test
    fun `updateTask should return false when dataSource throws exception`() = runTest {
        val task = TaskEntityTestData.normalTask
        coEvery { dataSource.updateTask(task) } throws RuntimeException("Update failed")

        val result = taskRepository.updateTask(task)

        Truth.assertThat(result).isFalse()
        coVerify { dataSource.updateTask(task) }
    }
}