package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.TaskEntity

interface TaskRepository {

    fun createTask(task: TaskEntity)
    fun getTaskById(id: String): TaskEntity
    fun getTasksByProjectId(id: String): List<TaskEntity>
    fun getTasksByStateId(id: String): List<TaskEntity>
    fun updateTask(task: TaskEntity)
    fun deleteTask(id: String)
}