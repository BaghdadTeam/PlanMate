package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.common.Result

class ListProjectsUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(): Result<List<ProjectEntity>> {
        return Result.Success(projectRepository.getAllProjects())
    }
}