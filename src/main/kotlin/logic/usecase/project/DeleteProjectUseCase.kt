package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.common.AccessPolicy
import org.baghdad.logic.usecase.common.Result
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    operator fun invoke(id: UUID, user: UserEntity): Result<Unit> {
        val access = AccessPolicy.requireAdmin(user)
        if (access is Result.Failure) return access
        if (taskRepository.getTasksByProjectId(id).isNotEmpty())
            return Result.Failure("Cannot delete project with active tasks.")
        projectRepository.deleteProject(id)
        return Result.Success()
    }
}