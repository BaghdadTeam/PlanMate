package org.baghdad.data.repositories.project

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.UUID

class ProjectRepositoryImpl(
    private val dataSource: ProjectDataSource
) : ProjectRepository {
    override suspend fun createProject(project: ProjectEntity) {
        dataSource.createProject(project)
    }

    override suspend fun getProjectById(id: UUID): ProjectEntity {
        return dataSource.getProjectById(id)
    }

    override suspend fun getAllProjects(): List<ProjectEntity> {
        return dataSource.getAllProjects()
    }

    override suspend fun deleteProject(id: UUID) {
        dataSource.deleteProject(id)
    }

    override suspend fun editProject(project: ProjectEntity) {
        dataSource.updateProject(project)
    }
}