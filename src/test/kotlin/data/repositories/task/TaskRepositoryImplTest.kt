package data.repositories.task

import com.google.common.truth.Truth
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.local.TaskDataSource
import org.baghdad.data.repositories.task.TaskRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import java.util.UUID
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
    fun `getAllTasks should return list from dataSource`() {
        val mockTasks = listOf(
            TaskEntityTestData.normalTask,
            TaskEntityTestData.normalTask.copy(id = UUID.randomUUID(), title = "Task 2"),
        )
        every { dataSource.loadTasks() } returns mockTasks

        val result = taskRepository.getAllTasks()

        Truth.assertThat(result).isEqualTo(mockTasks)
        verify { dataSource.loadTasks() }
    }

    @Test
    fun `createTask should call dataSource addTask`() {
        val task = TaskEntityTestData.normalTask

        taskRepository.createTask(task)

        verify { dataSource.addTask(task) }
    }

    @Test
    fun `getTaskById should return correct task`() {
        val task = TaskEntityTestData.normalTask
        val id = task.id
        every { dataSource.getTaskById(id) } returns task

        val result = taskRepository.getTaskById(id)

        Truth.assertThat(result).isEqualTo(task)
        verify { dataSource.getTaskById(id) }
    }

    @Test
    fun `getTasksByProjectId should return filtered tasks`() {
        val tasks = TaskEntityTestData.tasksInSameProject
        val projectID = UUID.randomUUID()
        every { dataSource.getTasksByProjectId(projectID) } returns tasks

        val result = taskRepository.getTasksByProjectId(projectID)

        Truth.assertThat(result).isEqualTo(tasks)
        verify { dataSource.getTasksByProjectId(projectID) }
    }

    @Test
    fun `getTasksByStateId should return filtered tasks`() {
        val tasks = TaskEntityTestData.tasksInSameState
        val stateID = UUID.randomUUID()
        every { dataSource.getTasksByStateId(stateID) } returns tasks

        val result = taskRepository.getTasksByStateId(stateID)

        Truth.assertThat(result).isEqualTo(tasks)
        verify { dataSource.getTasksByStateId(stateID) }
    }

    @Test
    fun `updateTask should call dataSource updateTask`() {
        val task = TaskEntityTestData.normalTask

        taskRepository.updateTask(task)

        verify { dataSource.updateTask(task) }
    }

    @Test
    fun `deleteTask should call dataSource deleteTask`() {
        val taskId = UUID.randomUUID()

        taskRepository.deleteTask(taskId)

        verify { dataSource.deleteTask(taskId) }
    }

    @Test
    fun `updateTask should return false when dataSource throws exception`() {
        val task = TaskEntityTestData.normalTask
        every { dataSource.updateTask(task) } throws RuntimeException("Update failed")

        val result = taskRepository.updateTask(task)

        Truth.assertThat(result).isFalse()
        verify { dataSource.updateTask(task) }
    }
}