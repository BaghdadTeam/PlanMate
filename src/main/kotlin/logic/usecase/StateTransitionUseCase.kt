package org.baghdad.logic.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository


class StateTransitionUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: StateRepository,
    private val auditRepository: AuditRepository
) {

    fun changeTaskState(taskId: String, newStateId: String, user: UserEntity): Result<Unit> {
        val task = taskRepository.getTaskById(taskId)
        val newState = stateRepository.getStateById(newStateId)
            ?: return Result.failure(Exception("State not found"))
        if (task.projectId != newState.projectId) {
            return Result.failure(Exception("State does not belong to the same project"))
        }

        val updatedTask = task.copy(stateId = newStateId)
        val updateSuccess = taskRepository.updateTask(updatedTask)
        if (!updateSuccess) return Result.failure(Exception("Failed to update task"))

        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val timestamp = currentTime.toString()
        val auditEntry = AuditEntity(
            entityType = "Task",
            entityId = taskId,
            action = "${user.username} changed task $taskId from state ${task.stateId} to $newStateId",
            user = user,
            timestamp = timestamp
        )
        auditRepository.addAuditEntry(auditEntry)

        println("Task ${task.id} moved from ${task.stateId} to $newStateId by ${user.username}")

        return Result.success(Unit)
    }
}
