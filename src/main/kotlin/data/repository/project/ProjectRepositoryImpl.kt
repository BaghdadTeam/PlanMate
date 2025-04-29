package org.baghdad.data.repository.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository

class ProjectRepositoryImpl: ProjectRepository {
    override fun createProject(project: ProjectEntity) {
        TODO("Not yet implemented")
    }

    override fun getProjectById(id: String): ProjectEntity? {
        TODO("Not yet implemented")
    }

    override fun getAllProjects(): List<ProjectEntity> {
        TODO("Not yet implemented")
    }

    override fun deleteProject(id: String): Boolean {
        TODO("Not yet implemented")
    }
}