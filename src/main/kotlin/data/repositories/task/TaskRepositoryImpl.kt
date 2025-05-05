package org.baghdad.data.repositories.task

import org.baghdad.data.local.TaskDataSource
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class   TaskRepositoryImpl(
    private val dataSource: TaskDataSource
) : TaskRepository {
    override fun getAllTasks(): List<TaskEntity> {
        return dataSource.loadTasks()
    }

    override fun createTask(task: TaskEntity) {
        dataSource.addTask(task)
    }

    override fun getTaskById(id: UUID): TaskEntity {
        return dataSource.getTaskById(id)
    }

    override fun getTasksByProjectId(id: UUID): List<TaskEntity> {
        return dataSource.getTasksByProjectId(id)
    }

    override fun getTasksByStateId(stateId: UUID): List<TaskEntity> {
        return dataSource.getTasksByStateId(stateId)
    }

    override fun updateTask(task: TaskEntity): Boolean {
        try {
            dataSource.updateTask(task)
            return true
        } catch (_: Exception) {
            return false
        }
    }

    override fun deleteTask(id: UUID) {
        dataSource.deleteTask(id)
    }
}