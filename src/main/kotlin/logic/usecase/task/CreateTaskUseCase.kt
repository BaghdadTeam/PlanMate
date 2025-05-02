package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.utils.getFormattedTimestamp

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val auditRepository: AuditRepository
) {

    operator fun invoke(task: TaskEntity, user: UserEntity) {
        val validatedTask = validateTask(task)
        taskRepository.createTask(validatedTask)

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
        if (task.stateId.isBlank()) throw TaskWithMissingStateIdException("Task state ID cannot be empty or blank")
        if (task.projectId.isBlank()) throw TaskWithMissingProjectIdException("Task project ID cannot be empty or blank")

        return task
    }
}