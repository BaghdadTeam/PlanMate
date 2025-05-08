package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository

) {
    suspend operator fun invoke(projectId: UUID, projectNewName: String, userId: UUID) {
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        if (projectNewName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")

        val existing = projectRepository.getProjectById(projectId)
        val updated = existing.copy(name = projectNewName)
        projectRepository.editProject(updated)
    }
}