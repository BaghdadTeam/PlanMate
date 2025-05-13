package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.entities.TaskStateEntity
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

        val newState = projectStatesRepository.getStateById(newStateId)
        val user = userRepository.getUserById(userId)

        logStateChange(task, currentState , newState, user)
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
        task: TaskEntity, oldState: TaskStateEntity, newState: TaskStateEntity, user: UserEntity
    ) {
        val description = "${user.username} changed task ${task.title} from state ${oldState.name} to ${newState.name}"

        val auditEntry = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = task.id,
            projectId = task.projectId,
            description = description,
            action = Action.Update,
            userId = user.id,
        )
        auditRepository.addAuditEntry(auditEntry)
    }
}