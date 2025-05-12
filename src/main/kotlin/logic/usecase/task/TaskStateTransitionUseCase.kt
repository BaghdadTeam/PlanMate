package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.NotFoundException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class TaskStateTransitionUseCase(
    private val taskRepository: TaskRepository,
    private val projectStatesRepository: ProjectStatesRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    suspend fun changeTaskState(taskId: UUID, newStateId: UUID, userId: UUID) {
        val task = taskRepository.getTaskById(taskId)

        val currentState = projectStatesRepository.getStateById(task.stateId)

        if (currentState.id == newStateId) {
            throw Exception("Task is already in the requested state. No changes made.")
        }
        validateNewState(task.projectId, newStateId)

        val updateSuccessful = updateTaskState(taskId, newStateId)
        if (!updateSuccessful) throw Exception("Failed to update task state")

        val user = userRepository.getUserById(userId)

        logStateChange(taskId, task.stateId, newStateId, user)
    }

    private suspend fun validateNewState(projectId: UUID, newStateId: UUID) =
        projectStatesRepository.getStateById(newStateId).takeIf { it.projectId == projectId }
            ?: throw NotFoundException("State not found in this project")

    private suspend fun updateTaskState(taskId: UUID, newStateId: UUID): Boolean {
        val task = taskRepository.getTaskById(taskId)
        val updatedTask = task.copy(stateId = newStateId)
        return taskRepository.updateTask(updatedTask)
    }

    private suspend fun logStateChange(
        taskId: UUID, oldStateId: UUID, newStateId: UUID, user: UserEntity
    ) {
        val action = "${user.username} changed task $taskId from state $oldStateId to $newStateId"

        val auditEntry = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = taskId,
            action = action,
            user = user,
        )
        auditRepository.addAuditEntry(auditEntry)
    }
}