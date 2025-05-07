package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository

class GetAllProjectsUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke():List<ProjectEntity>{
        return projectRepository.getAllProjects()
    }
}