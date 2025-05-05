package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.TaskEntity
import java.util.UUID

interface TaskRepository {

    fun createTask(task: TaskEntity)
    fun getTaskById(id: UUID): TaskEntity
    fun getTasksByProjectId(id: UUID): List<TaskEntity>
    fun getTasksByStateId(id: UUID): List<TaskEntity>
    fun updateTask(task: TaskEntity): Boolean
    fun deleteTask(id: UUID)
    fun getAllTasks(): List<TaskEntity>
}