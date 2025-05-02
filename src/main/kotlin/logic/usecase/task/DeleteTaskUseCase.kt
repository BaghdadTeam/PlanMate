package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.utils.getFormattedTimestamp

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository
) {

    operator fun invoke(taskId: String, user: UserEntity) {
        val task = taskRepository.getTaskById(taskId)
        taskRepository.deleteTask(taskId)
        val audit = logTaskDeletion(task, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun logTaskDeletion(task: TaskEntity, user: UserEntity): AuditEntity {

        val action = "has been deleted task ${task.title}"
        return AuditEntity(
            entityType = Entities.Task.name,
            timestamp = getFormattedTimestamp(),
            entityId = task.id.toString(),
            action = action,
            user = user,
        )
    }
}