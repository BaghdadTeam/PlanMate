package logic.usecase.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.*

class FakeProjectRepository : ProjectRepository {
    private val projects = mutableListOf<ProjectEntity>()

    override fun createProject(project: ProjectEntity) {
        projects.add(project)
    }

    override fun editProject(project: ProjectEntity) {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index >= 0) {
            projects[index] = project
        }
    }

    override fun deleteProject(projectId: String): Boolean {
        return projects.removeAll { it.id.toString() == projectId }
    }

    override fun getProjectById(projectId: String): ProjectEntity? {
        return projects.find { it.id.toString() == projectId }
    }

    override fun getAllProjects(): List<ProjectEntity> {
        return projects
    }
}
