package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.common.AccessPolicy.requireAdmin
import org.baghdad.logic.usecase.common.Result
import java.util.UUID

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectName: String, user: UserEntity): Result<Unit> {
        requireAdmin(user = user)
        val projectId = UUID.randomUUID()
        val project = ProjectEntity(id = projectId, name = projectName, creatorId = user.id.toString())
        projectRepository.createProject(project)
        return Result.Success()
    }
}