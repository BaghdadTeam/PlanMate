package org.baghdad.logic.usecase

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.TaskRepository
import org.baghdad.logic.usecase.common.AccessPolicy.requireAdmin
import java.util.UUID

class ProjectsServicesUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) {
    fun create(projectName: String, user: UserEntity) {
        requireAdmin(user = user)
        val projectId = UUID.randomUUID()
        val project = ProjectEntity(id = projectId, name = projectName, creatorId = user.id)
        projectRepository.createProject(project)
    }

    fun edit(projectId: UUID, newProjectName: String, user: UserEntity) {
        requireAdmin(user = user)
        val existingProject = projectRepository.getProjectById(projectId.toString())
            ?: throw NoSuchElementException("Project not found!.")
        val updateProject = existingProject.copy(
            name = newProjectName,
        )
        projectRepository.editProject(updateProject)
    }

    fun delete(projectId: UUID, user: UserEntity) {
        requireAdmin(user = user)
        val tasks = taskRepository.getTasksByProjectId(projectId.toString())
        if (tasks.isNotEmpty())
            throw IllegalStateException("Can't delete project with active task!.")
        projectRepository.deleteProject(projectId.toString())
    }

    fun list(): List<ProjectEntity> = projectRepository.getAllProjects()

    fun createProjectId(name: String): String {
        val clean = name.replace("\\s+".toRegex(), "").take(5).uppercase()
        val suffix = UUID.randomUUID().toString().take(4)
        return "PRJ-${clean}-$suffix"
    }
}