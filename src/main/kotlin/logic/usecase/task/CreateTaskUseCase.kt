package org.baghdad.logic.usecase.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(task: TaskEntity, userId: UUID) {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
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
        val description = "created task ${task.title}"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = task.id,
            projectId = task.projectId,
            description = description,
            action = Action.Create,
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