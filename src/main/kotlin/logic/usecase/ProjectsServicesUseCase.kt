package org.baghdad.logic.usecase

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.common.AccessPolicy
import org.baghdad.logic.usecase.common.AccessPolicy.requireAdmin
import java.util.UUID
import org.baghdad.logic.usecase.common.AccessPolicy.requireAdmin
import org.baghdad.logic.usecase.common.Result
import java.util.*

class ProjectsServicesUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    fun create(name: String, user: UserEntity): Result<Unit> {
        val access = AccessPolicy.requireAdmin(user)
        if (access is Result.Failure) return access
        val projectId = UUID.randomUUID()
        val project = ProjectEntity(projectId, name, user.id.toString())
        projectRepository.createProject(project)
        return Result.Success()
    }

    fun edit(id: UUID, newName: String, user: UserEntity): Result<Unit> {
        val access = AccessPolicy.requireAdmin(user)
        if (access is Result.Failure) return access
        val existing = projectRepository.getProjectById(id.toString())
            ?: return Result.Failure("Project not found.")
        val updated = existing.copy(name = newName)
        projectRepository.editProject(updated)
        return Result.Success()
    }

    fun delete(id: UUID, user: UserEntity): Result<Unit> {
        val access = AccessPolicy.requireAdmin(user)
        if (access is Result.Failure) return access
        if (taskRepository.getTasksByProjectId(id.toString()).isNotEmpty())
            return Result.Failure("Cannot delete project with active tasks.")
        projectRepository.deleteProject(id.toString())
        return Result.Success()
    }

    fun list(): Result<List<ProjectEntity>> {
        return Result.Success(projectRepository.getAllProjects())
    }
}
