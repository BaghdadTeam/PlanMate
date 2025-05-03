package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.common.AccessPolicy
import org.baghdad.logic.usecase.common.Result
import java.util.UUID

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(id: UUID, newName: String, user: UserEntity): Result<Unit> {
        val access = AccessPolicy.requireAdmin(user)
        if (access is Result.Failure) return access
        val existing = projectRepository.getProjectById(id.toString())
            ?: return Result.Failure("Project not found.")
        val updated = existing.copy(name = newName)
        projectRepository.editProject(updated)
        return Result.Success()
    }
}