package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.ProjectNameIsEmptyException
import org.baghdad.logic.repositories.ProjectRepository


class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
) {

    operator fun invoke(user: UserEntity, project: ProjectEntity) {
        if (project.name.isBlank()) throw ProjectNameIsEmptyException("")
        if (user.type == UserType.Admin) {
            projectRepository.editProject(project)
        }
    }
}