package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(projectId: UUID,userId : UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name != UserType.Admin.name) throw AccessDeniedException("Not authorized")
        projectRepository.deleteProject(projectId)
    }
}