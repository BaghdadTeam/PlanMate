package org.baghdad.data.repository.project

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.UUID

class ProjectRepositoryImpl(
    private val dataSource: ProjectDataSource
) : ProjectRepository {
    override fun createProject(project: ProjectEntity) {
        dataSource.createProject(project)
    }

    override fun getProjectById(id: UUID): ProjectEntity? {
        return dataSource.getProjectById(id)
    }

    override fun getAllProjects(): List<ProjectEntity> {
        return dataSource.getProjects()
    }

    override fun deleteProject(id: UUID) {
        dataSource.deleteProject(id)
    }

    override fun editProject(project: ProjectEntity) {
        dataSource.editProject(project)
    }
}