package org.baghdad.logic.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class StateTransitionUseCase(
    private val taskRepository: TaskRepository,
    private val projectStatesRepository: ProjectStatesRepository,
    private val auditRepository: AuditRepository
) {
    fun changeTaskState(taskId: UUID, newStateId: UUID, user: UserEntity) {
        val task = taskRepository.getTaskById(taskId.toString())

        val currentState = projectStatesRepository.getStateById(UUID.fromString(task.stateId))
            ?: throw Exception("Current state not found")

        if (currentState.id == newStateId) {
            println("Task is already in the requested state. No changes made.")
            return
        }
        validateNewState(UUID.fromString(task.projectId), newStateId)

        val updateSuccessful = updateTaskState(taskId, newStateId)
        if (!updateSuccessful) throw Exception("Failed to update task state")

        logStateChange(taskId, UUID.fromString(task.stateId), newStateId, user)
    }

    private fun validateNewState(projectId: UUID, newStateId: UUID) =
        projectStatesRepository.getStateById(newStateId)?.takeIf { it.projectId == projectId }
            ?: throw NotFoundException("State not found in this project")

    private fun updateTaskState(taskId: UUID, newStateId: UUID): Boolean {
        val task = taskRepository.getTaskById(taskId.toString())
        val updatedTask = task.copy(stateId = newStateId.toString())
        return taskRepository.updateTask(updatedTask)
    }

    private fun logStateChange(
        taskId: UUID, oldStateId: UUID, newStateId: UUID, user: UserEntity
    ) {
        val timestamp =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        val action = "${user.username} changed task $taskId from state $oldStateId to $newStateId"
        println("Task $taskId moved from $oldStateId to $newStateId by ${user.username}")

        val auditEntry = AuditEntity(
            entityType = "Task",
            entityId = taskId.toString(),
            action = action,
            user = user,
            timestamp = timestamp
        )
        auditRepository.addAuditEntry(auditEntry)
    }
}
