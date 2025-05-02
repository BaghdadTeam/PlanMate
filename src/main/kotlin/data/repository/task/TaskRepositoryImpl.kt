package org.baghdad.data.repository.task

import org.baghdad.data.local.TaskDataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository

class TaskRepositoryImpl(
    private val dataSource: TaskDataSource
) : TaskRepository {
    override fun getAllTasks(): List<TaskEntity> {
        return dataSource.loadTasks()
    }
    override fun createTask(task: TaskEntity) {
        dataSource.addTask(task)
    }

    override fun getTaskById(id: String): TaskEntity {
        return dataSource.getTaskById(id)
    }

    override fun getTasksByProjectId(projectId: String): List<TaskEntity> {
        return dataSource.getTasksByProjectId(projectId)
    }

    override fun getTasksByStateId(stateId: String): List<TaskEntity> {
        return dataSource.getTasksByStateId(stateId)
    }

    override fun updateTask(task: TaskEntity) {
        dataSource.updateTask(task)
    }

    override fun deleteTask(id: String) {
        dataSource.deleteTask(id)
    }
}