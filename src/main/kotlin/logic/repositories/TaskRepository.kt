package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.TaskEntity
import java.util.UUID

interface TaskRepository {

    suspend fun createTask(task: TaskEntity)
    suspend fun getTaskById(id: UUID): TaskEntity  // Remove this task
    suspend fun getTasksByProjectId(id: UUID): List<TaskEntity>
    suspend fun getTasksByStateId(id: UUID): List<TaskEntity>
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun deleteTask(id: UUID)
    suspend fun getAllTasks(): List<TaskEntity>
}