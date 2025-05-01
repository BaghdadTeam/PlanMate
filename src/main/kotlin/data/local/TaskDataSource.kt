package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException

class TaskDataSource(
    private val dataSource: DataSource<TaskEntity>
) {

    fun loadTasks(): List<TaskEntity> {
        return dataSource.loadAll()
    }

    fun addTask(task: TaskEntity) {
        dataSource.append(task)
    }

    fun getTasksByProjectId(projectId: String): List<TaskEntity> {
        val tasks = loadTasks().filter { it.projectId == projectId }
        if (tasks.isEmpty()) {
            throw TasksNotFoundException("No tasks found for project ID: $projectId")
        }
        return tasks
    }

    fun getTaskById(taskId: String): TaskEntity {
        return loadTasks().firstOrNull { it.id.toString() == taskId }
            ?: throw TasksNotFoundException("Task with id $taskId not found")
    }

    fun getTasksByStateId(stateId: String): List<TaskEntity> {
        val tasks = loadTasks().filter { it.stateId == stateId }
        if (tasks.isEmpty()) {
            throw TasksNotFoundException("No tasks found for state ID: $stateId")
        }
        return tasks
    }

    fun updateTask(task: TaskEntity) {
        val allTasks = loadTasks().toMutableList()
        val taskIndex = loadTasks().indexOfFirst { it.id == task.id }

        if (taskIndex != -1) {
            allTasks[taskIndex] = task
            dataSource.update(allTasks)
        } else {
            throw TasksNotFoundException("Task with id ${task.id} not found")
        }
    }

    fun deleteTask(taskId: String) {
        val allTasks = loadTasks().toMutableList()
        val removed = allTasks.removeIf { it.id.toString() == taskId }

        if (!removed) {
            throw TasksNotFoundException("Task with id $taskId not found")
        }
        dataSource.update(allTasks)
    }
}