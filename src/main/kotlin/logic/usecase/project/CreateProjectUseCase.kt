package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(projectName: String, userId : UUID){
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        if (projectName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")
        val project = ProjectEntity(name = projectName, creatorId = user.id)
        projectRepository.createProject(project)
    }
}