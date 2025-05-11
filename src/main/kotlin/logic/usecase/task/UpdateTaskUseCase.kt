package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class UpdateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(newTask: TaskEntity, userId: UUID) {
        val oldTask = taskRepository.getTaskById(newTask.id)

        val validatedTask = validateNewTask(newTask)

        val result = taskRepository.updateTask(validatedTask)
        val user = userRepository.getUserById(userId)

        if (result) {
            logTaskUpdate(validatedTask, oldTask, user)
        } else {
            throw Exception("Failed to update task")
        }
    }

    private suspend fun logTaskUpdate(
        newTask: TaskEntity,
        oldTask: TaskEntity,
        user: UserEntity
    ) {
        val changes = mutableListOf<String>()

        if (newTask.title != oldTask.title) {
            changes += "title changed from “${oldTask.title}” to “${newTask.title}”"
        }
        if (newTask.description != oldTask.description) {
            changes += "description changed from “${oldTask.description}” to “${newTask.description}”"
        }

        // if nothing actually changed, don’t write an audit entry
        if (changes.isEmpty()) return

        // join them with “and” for readability
        val action = "Task “${oldTask.title}” was updated: ${changes.joinToString(" and ")}"

        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = newTask.projectId,
            action = action,
            userId = user.id,
        )
        auditRepository.addAuditEntry(audit)
    }

    private fun validateNewTask(newTask: TaskEntity): TaskEntity {
        if (newTask.title.isBlank()) throw TaskWithMissingTitleException("Title cannot be empty or blank")

        if (newTask.description.isBlank()) throw TaskWithMissingDescriptionException("Description cannot be empty blank")

        return newTask
    }
}