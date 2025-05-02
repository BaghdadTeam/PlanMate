package data.repository.task

import com.google.common.base.Verify.verify
import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.local.TaskDataSource
import org.baghdad.data.repository.task.TaskRepositoryImpl
import org.junit.jupiter.api.Assertions.*
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

        assertThat(result).isEqualTo(mockTasks)
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
        every { dataSource.getTaskById(id.toString()) } returns task

        val result = taskRepository.getTaskById(id.toString())

        assertThat(result).isEqualTo(task)
        verify { dataSource.getTaskById(id.toString()) }
    }

    @Test
    fun `getTasksByProjectId should return filtered tasks`() {
        val tasks = TaskEntityTestData.tasksInSameProject
        every { dataSource.getTasksByProjectId("p1") } returns tasks

        val result = taskRepository.getTasksByProjectId("p1")

        assertThat(result).isEqualTo(tasks)
        verify { dataSource.getTasksByProjectId("p1") }
    }

    @Test
    fun `getTasksByStateId should return filtered tasks`() {
        val tasks = TaskEntityTestData.tasksInSameState
        every { dataSource.getTasksByStateId("s1") } returns tasks

        val result = taskRepository.getTasksByStateId("s1")

        assertThat(result).isEqualTo(tasks)
        verify { dataSource.getTasksByStateId("s1") }
    }

    @Test
    fun `updateTask should call dataSource updateTask`() {
        val task = TaskEntityTestData.normalTask

        taskRepository.updateTask(task)

        verify { dataSource.updateTask(task) }
    }

    @Test
    fun `deleteTask should call dataSource deleteTask`() {
        val taskId = "1"

        taskRepository.deleteTask(taskId)

        verify { dataSource.deleteTask(taskId) }
    }
}