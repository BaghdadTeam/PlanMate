package org.baghdad.logic.usecase.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(taskId: UUID, userId: UUID) {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        val task = taskRepository.getTaskById(taskId)
        taskRepository.deleteTask(taskId)

        val user = userRepository.getUserById(userId)

        val audit = logTaskDeletion(task, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun logTaskDeletion(task: TaskEntity, user: UserEntity): AuditLogEntity {

        val description = "has been deleted task ${task.title}"
        return AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = task.id,
            projectId = task.projectId,
            description = description,
            action = Action.Delete,
            userId = user.id,
        )
    }
}