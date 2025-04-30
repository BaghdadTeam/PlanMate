package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.ProjectNameIsEmptyException
import org.baghdad.logic.repositories.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(user: UserEntity, projectName: String) {
        if (projectName.isBlank()) throw ProjectNameIsEmptyException("Project name should not be empty or blank")

        if (user.type == UserType.Admin) {
            projectRepository.createProject(
                ProjectEntity(
                    name = projectName,
                    creatorId = user.id.toString()
                )
            )
        }
    }
}