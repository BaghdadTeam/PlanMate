package org.baghdad.logic.usecase.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.TaskRepository

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository,
    private val sessionManager: SessionManager,
) {

    suspend operator fun invoke(): List<TaskEntity> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        return taskRepository.getAllTasks()
    }
}