package org.baghdad.logic.repositories

import org.baghdad.logic.entities.TaskEntity

interface TaskRepository {

    fun createTask(task: TaskEntity)
    fun getTaskById(id: String): TaskEntity?
    fun getTasksByProjectId(projectId: String): List<TaskEntity>
    fun updateTask(task: TaskEntity)
    fun deleteTask(id: String)
}