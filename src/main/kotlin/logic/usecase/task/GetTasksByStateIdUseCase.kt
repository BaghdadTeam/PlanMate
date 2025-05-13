package org.baghdad.logic.usecase.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetTasksByStateIdUseCase(
    private val taskRepository: TaskRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(stateId: UUID): List<TaskEntity> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        return taskRepository.getTasksByStateId(stateId)
    }
}