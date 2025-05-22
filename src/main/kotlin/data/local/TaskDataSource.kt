package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import java.util.*

class TaskDataSource(
    private val dataSource: DataSource<TaskEntity>
) {

    suspend fun loadTasks(): List<TaskEntity> {
        return dataSource.loadAll()
    }

    suspend fun addTask(task: TaskEntity) {
        return dataSource.append(task)

    }

    suspend fun getTasksByProjectId(projectId: UUID): List<TaskEntity> {
        return loadTasks().filter { it.projectId == projectId }
            .takeIf { it.isNotEmpty() } ?: throw TasksNotFoundException()
    }

    suspend fun getTaskById(taskId: UUID): TaskEntity {
        return loadTasks().firstOrNull { it.id == taskId }
            ?: throw TasksNotFoundException()
    }

    suspend fun getTasksByStateId(stateId: UUID): List<TaskEntity> {
        val tasks = loadTasks().filter { it.stateId == stateId }
        if (tasks.isEmpty()) {
            throw TasksNotFoundException()
        }
        return tasks
    }

    suspend fun updateTask(task: TaskEntity) {
        loadTasks().find { it.id == task.id }
            ?: throw TasksNotFoundException()
        return dataSource.update(task)
    }

    suspend fun deleteTask(taskId: UUID) {
        val foundTask = loadTasks().find { it.id == taskId }
            ?: throw TasksNotFoundException()
        return dataSource.delete(foundTask)
    }
}