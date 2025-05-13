package org.baghdad.logic.usecase.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke():List<ProjectEntity>{
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        return projectRepository.getAllProjects()
    }
}