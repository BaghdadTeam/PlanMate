package org.baghdad.data.repository.task

import org.baghdad.logic.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.storage.task.TaskStorage
import org.baghdad.utils.customizedExceptions.*

class TaskRepositoryImpl(
    val storage: TaskStorage
) : TaskRepository {

    override fun createTask(task: TaskEntity): Boolean {

        validateTask(task)
        return storage.save(task)
    }

    override fun getTaskById(id: String): TaskEntity {

        val tasks = storage.getAll()

        return tasks.first {
            it.id.toString() == id
        }
    }

    override fun getTasksByProjectId(projectId: String): List<TaskEntity> {
        return emptyList()
    }

    override fun updateTask(task: TaskEntity): Boolean {
        return false
    }

    override fun deleteTask(id: String): Boolean {
        return false
    }

    private fun validateTask(task: TaskEntity) {
        val validations = listOf(
            task.title to { TaskMissingTitleException() },
            task.description to { TaskMissingDescriptionException() },
            task.stateId to { TaskMissingStateIdException() },
            task.projectId to { TaskMissingProjectIdException() },
            task.creatorId to { TaskMissingCreatorIdException() }
        )

        validations.forEach { (value, exceptionSupplier) ->
            if (value.isBlank()) throw exceptionSupplier()
        }
    }
}