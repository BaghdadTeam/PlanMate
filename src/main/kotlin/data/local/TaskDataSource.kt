package org.baghdad.data.local

import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import java.util.UUID

class TaskDataSource(
    private val dataSource: DataSource<TaskEntity>
) {

    fun loadTasks(): List<TaskEntity> {
        return runBlocking {
            dataSource.loadAll()
        }
    }

    fun addTask(task: TaskEntity) {
        return runBlocking {
            dataSource.append(task)
        }
    }

    fun getTasksByProjectId(projectId: UUID): List<TaskEntity> {
        val tasks = loadTasks().filter { it.projectId == projectId }
        if (tasks.isEmpty()) {
            throw TasksNotFoundException("No tasks found for project ID: $projectId")
        }
        return tasks
    }

    fun getTaskById(taskId: UUID): TaskEntity {
        return loadTasks().firstOrNull { it.id == taskId }
            ?: throw TasksNotFoundException("Task with id $taskId not found")
    }

    fun getTasksByStateId(stateId: UUID): List<TaskEntity> {
        val tasks = loadTasks().filter { it.stateId == stateId }
        if (tasks.isEmpty()) {
            throw TasksNotFoundException("No tasks found for state ID: $stateId")
        }
        return tasks
    }

    fun updateTask(task: TaskEntity) {
        return runBlocking {
            val foundTask = loadTasks().find { it.id == task.id }
                ?: throw TasksNotFoundException("Task with id ${task.id} not found")

            dataSource.update(foundTask)
        }
    }

    fun deleteTask(taskId: UUID) {
        return runBlocking {
            val foundTask = loadTasks().find { it.id == taskId }
                ?: throw TasksNotFoundException("Task with id $taskId not found")

            dataSource.delete(foundTask)
        }
    }
}