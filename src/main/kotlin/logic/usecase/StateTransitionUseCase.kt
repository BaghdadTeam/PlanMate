package org.baghdad.logic.usecase

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.StateExceptions.NotFoundException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository

class StateTransitionUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: StateRepository,
    private val auditRepository: AuditRepository
) {
    fun changeTaskState(taskId: String, newStateId: String, user: UserEntity) {
        val task = taskRepository.getTaskById(taskId)

        val currentState = stateRepository.getStateById(task.stateId)
            ?: throw Exception("Current state not found")

        if (currentState.id.toString() == newStateId) {
            println("Task is already in the requested state. No changes made.")
            return
        }
        validateNewState(task.projectId, newStateId)

        val updateSuccessful = updateTaskState(taskId, newStateId)
        if (!updateSuccessful) throw Exception("Failed to update task state")

        logStateChange(taskId, task.stateId, newStateId, user)
    }

    private fun validateNewState(projectId: String, newStateId: String) =
        stateRepository.getStateById(newStateId)?.takeIf { it.projectId == projectId }
            ?: throw NotFoundException("State not found in this project")

    private fun updateTaskState(taskId: String, newStateId: String): Boolean {
        val task = taskRepository.getTaskById(taskId)
            ?: throw Exception("Task not found")
        val updatedTask = task.copy(stateId = newStateId)
        return taskRepository.updateTask(updatedTask)
    }

    private fun logStateChange(
        taskId: String, oldStateId: String, newStateId: String, user: UserEntity
    ) {
        val timestamp =
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        val action = "${user.username} changed task $taskId from state $oldStateId to $newStateId"
        println("Task $taskId moved from $oldStateId to $newStateId by ${user.username}")

        val auditEntry = AuditEntity(
            entityType = "Task",
            entityId = taskId,
            action = action,
            user = user,
            timestamp = timestamp
        )
        auditRepository.addAuditEntry(auditEntry)
    }
}
