package data.local

import com.google.common.truth.Truth.assertThat
import helpers.task.TaskEntityTestData
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.baghdad.data.datasource.DataSource
import org.baghdad.data.local.TaskDataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class TaskDataSourceTest {

    private lateinit var taskDataSource: TaskDataSource
    private lateinit var dataSource: DataSource<TaskEntity>

    @BeforeEach
    fun setup() {
        dataSource = mockk(relaxed = true)
        taskDataSource = TaskDataSource(dataSource)
    }

    @Test
    fun `should return all tasks that is found in the data source`() {
        // Given
        val tasks = TaskEntityTestData.tasks
        coEvery { dataSource.loadAll() } returns tasks

        // When
        val result = taskDataSource.loadTasks()

        // Then
        assertThat(result).isEqualTo(tasks)
    }

    @Test
    fun `addTask should call append on data source`() {
        // Given
        val task = TaskEntityTestData.normalTask
        coEvery { dataSource.append(task) } just Runs

        // When
        taskDataSource.addTask(task)

        // Then
        coVerify { dataSource.append(task) }
    }

    @Test
    fun `should return task with the same id when calling getTaskById`() {
        // Given
        val tasks = TaskEntityTestData.tasks
        coEvery { dataSource.loadAll() } returns tasks

        // When
        val result = taskDataSource.getTaskById(tasks[0].id)

        // Then
        assertThat(result).isEqualTo(tasks[0])
    }

    @Test
    fun `should throw TasksNotFoundException if task not found`() {
        // Given
        val randomTask = TaskEntityTestData.randomTask
        coEvery { dataSource.loadAll() } returns listOf(randomTask)
        val taskId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<TasksNotFoundException> {
            taskDataSource.getTaskById(taskId)
        }
        assertThat(exception.message).isEqualTo("Task with id $taskId not found")
    }

    @Test
    fun `should return matching tasks with the same projectId when calling getTasksByProjectId`() {
        // Given
        val tasksInSameProject = TaskEntityTestData.tasksInSameProject
        val tasks = TaskEntityTestData.tasks
        coEvery { dataSource.loadAll() } returns tasks

        // When
        val result = taskDataSource.getTasksByProjectId(tasksInSameProject[0].projectId)

        // Then
        assertThat(result).isEqualTo(tasksInSameProject)
    }

    @Test
    fun `should throws TasksNotFoundException if there is no tasks available for the project`() {
        // Given
        val randomTask = TaskEntityTestData.randomTask
        coEvery { dataSource.loadAll() } returns listOf(randomTask)
        val projectId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<TasksNotFoundException> {
            taskDataSource.getTasksByProjectId(projectId)
        }
        assertThat(exception.message).isEqualTo("No tasks found for project ID: $projectId")
    }

    @Test
    fun `should return matching tasks with the same stateId when calling getTasksByStateId`() {
        // Given
        val tasksInSameState = TaskEntityTestData.tasksInSameState
        coEvery { dataSource.loadAll() } returns tasksInSameState

        // When
        val result = taskDataSource.getTasksByStateId(tasksInSameState[0].stateId)

        // Then
        assertThat(result).isEqualTo(tasksInSameState)
    }

    @Test
    fun `should throws TasksNotFoundException if there is no tasks available for the stateId`() {
        // Given
        val randomTask = TaskEntityTestData.randomTask
        coEvery { dataSource.loadAll() } returns listOf(randomTask)
        val stateId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<TasksNotFoundException> {
            taskDataSource.getTasksByStateId(stateId)
        }
        assertThat(exception.message).isEqualTo("No tasks found for state ID: $stateId")
    }

    @Test
    fun `updateTask should replace existing task with same ID`() = runTest {
        val existingTasks = TaskEntityTestData.tasks
        val updatedTask = TaskEntityTestData.normalTask.copy(
            title = "Updated Title",
            description = "Updated description"
        )

        coEvery { dataSource.loadAll() } returns existingTasks
        coEvery { dataSource.update(updatedTask) } just Runs

        taskDataSource.updateTask(updatedTask)

        coVerify {
            dataSource.update(match {
                it.id == updatedTask.id && it.title == "Updated Title" && it.description == "Updated description"
            })
        }
    }

    @Test
    fun `updateTask should throw when task not found`() {
        val existingTasks = TaskEntityTestData.tasks
        val randomId = UUID.randomUUID()
        val unknownTask =
            TaskEntityTestData.randomTask.copy(id = randomId)

        coEvery { dataSource.loadAll() } returns existingTasks

        val exception = assertThrows<TasksNotFoundException> {
            taskDataSource.updateTask(unknownTask)
        }

        assertThat(exception.message).contains("Task with id $randomId not found")
    }

    @Test
    fun `deleteTask should remove task if it exists`() {
        val existingTasks = TaskEntityTestData.tasks
        val taskToRemove = TaskEntityTestData.normalTask

        coEvery { dataSource.loadAll() } returns existingTasks
        coEvery { dataSource.update(any()) } just Runs

        taskDataSource.deleteTask(taskToRemove.id)

        coVerify {
            dataSource.delete(match { it.id == taskToRemove.id })
        }
    }

    @Test
    fun `deleteTask should throw when task not found`() {
        val existingTasks = TaskEntityTestData.tasks
        val unknownId = UUID.randomUUID()
        coEvery { dataSource.loadAll() } returns existingTasks

        val exception = assertThrows<TasksNotFoundException> {
            taskDataSource.deleteTask(unknownId)
        }

        assertThat(exception.message).contains("Task with id $unknownId not found")
    }
}
