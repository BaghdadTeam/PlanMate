package org.baghdad.data.repositories.project

import org.baghdad.data.local.ProjectDataSource
import org.baghdad.data.repositories.toDto
import org.baghdad.data.repositories.toEntity
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.UUID

class ProjectRepositoryImpl(
    private val dataSource: ProjectDataSource
) : ProjectRepository {

    override suspend fun createProject(project: ProjectEntity) {
        dataSource.createProject(project.toDto())
    }

    override suspend fun getProjectById(id: UUID): ProjectEntity {
        return dataSource.getProjectById(id).toEntity()
    }

    override suspend fun getAllProjects(): List<ProjectEntity> {
        return dataSource.getAllProjects().map { it.toEntity() }
    }

    override suspend fun deleteProject(id: UUID) {
        dataSource.deleteProject(id)
    }


    override suspend fun editProject(project: ProjectEntity) {
        dataSource.updateProject(project.toDto())
    }
}
