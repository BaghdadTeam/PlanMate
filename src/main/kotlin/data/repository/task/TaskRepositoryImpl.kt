package org.baghdad.data.repository.task

import org.baghdad.logic.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.storage.task.TaskStorage

class TaskRepositoryImpl(
    val storage: TaskStorage
): TaskRepository {
    override fun createTask(task: TaskEntity): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTaskById(id: String): TaskEntity? {
        TODO("Not yet implemented")
    }

    override fun getTasksByProjectId(projectId: String): List<TaskEntity> {
        TODO("Not yet implemented")
    }

    override fun updateTask(task: TaskEntity):Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteTask(id: String):Boolean {
        TODO("Not yet implemented")
    }
}