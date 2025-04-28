package org.baghdad.data.repository.task

import org.baghdad.data.storage.task.TaskStorage
import org.baghdad.logic.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.utils.customizedExceptions.*
import java.util.UUID

class TaskRepositoryImpl(
    val storage: TaskStorage
) : TaskRepository {

    override fun createTask(task: TaskEntity): Boolean {

        validateTask(task)
        return storage.save(task)
    }

    override fun getTaskById(id: String): TaskEntity {

        if (id.isBlank()) throw TaskWithEmptyIDException("The id should not be empty or blank")

        val taskId = parseUUID(id)

        return storage.getAll()
            .firstOrNull { it.id == taskId }
            ?: throw TaskNotFoundException("There is no task with the given ID")
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
            task.title to { TaskMissingTitleException("The task shouldn't have an empty title") },
            task.description to { TaskMissingDescriptionException("The task shouldn't have an empty Description") },
            task.stateId to { TaskMissingStateIdException("The task should be related to a state") },
            task.projectId to { TaskMissingProjectIdException("The task should be related to a project") },
            task.creatorId to { TaskMissingCreatorIdException("The task should contain it's creator Id") }
        )

        validations.forEach { (value, exceptionSupplier) ->
            if (value.isBlank()) throw exceptionSupplier()
        }
    }

    private fun parseUUID(id: String): UUID {
        return try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidTaskIdException("The Given id is not of type UUID")
        }
    }

}