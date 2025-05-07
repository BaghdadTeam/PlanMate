package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.getFormattedTimestamp
import java.util.UUID

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(taskId: UUID, userId: UUID) {
        val task = taskRepository.getTaskById(taskId)
        taskRepository.deleteTask(taskId)

        val user = userRepository.getUserById(userId)

        val audit = logTaskDeletion(task, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun logTaskDeletion(task: TaskEntity, user: UserEntity): AuditLogEntity {

        val action = "has been deleted task ${task.title}"
        return AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityId = task.id,
            action = action,
            user = user,
        )
    }
}