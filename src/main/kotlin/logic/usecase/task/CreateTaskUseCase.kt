package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.getFormattedTimestamp
import java.util.UUID

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(task: TaskEntity, userId: UUID) {
        val validatedTask = validateTask(task)
        taskRepository.createTask(validatedTask)

        val user = userRepository.getUserById(userId)
        val auditTask = logTaskCreation(validatedTask, user)

        auditRepository.addAuditEntry(auditTask)
    }

    private fun logTaskCreation(
        task: TaskEntity,
        user: UserEntity
    ): AuditEntity {
        val action = "created task ${task.title}"
        val audit = AuditEntity(
            entityType = Entities.Task.name,
            entityId = task.id.toString(),
            action = action,
            user = user,
            timestamp = getFormattedTimestamp(),
        )
        return audit
    }

    private fun validateTask(task: TaskEntity): TaskEntity {
        if (task.title.isBlank()) throw TaskWithMissingTitleException("Task title cannot be empty or blank")
        if (task.description.isBlank()) throw TaskWithMissingDescriptionException("Task description cannot be empty or blank")

        return task
    }
}