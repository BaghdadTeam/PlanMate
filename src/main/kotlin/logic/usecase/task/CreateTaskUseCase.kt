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

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(task: TaskEntity, userId: UUID) {
        val validatedTask = validateTask(task)
        taskRepository.createTask(validatedTask)

        val user = userRepository.getUserById(userId)
        val auditTask = logTaskCreation(validatedTask, user)

        auditRepository.addAuditEntry(auditTask)
    }

    private fun logTaskCreation(
        task: TaskEntity,
        user: UserEntity
    ): AuditLogEntity {
        val action = "created task ${task.title}"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = task.id,
            projectId = task.projectId,
            action = action,
            userId = user.id,
        )
        return audit
    }

    private fun validateTask(task: TaskEntity): TaskEntity {
        if (task.title.isBlank()) throw TaskWithMissingTitleException("Task title cannot be empty or blank")
        if (task.description.isBlank()) throw TaskWithMissingDescriptionException("Task description cannot be empty or blank")

        return task
    }
}