package org.baghdad.data.repositories.project

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.ProjectDto
import org.baghdad.data.local.ProjectDataSource
import org.baghdad.data.mapper.toDto
import org.baghdad.data.mapper.toEntity
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import org.baghdad.logic.repositories.ProjectRepository
import java.util.UUID

class ProjectRepositoryImpl(
    private val dataSource: DataSource<ProjectDto>
) : ProjectRepository {
    override suspend fun createProject(project: ProjectEntity) {
        dataSource.append(project.toDto())
    }

    override suspend fun getProjectById(id: UUID): ProjectEntity {
        return dataSource.loadAll()
            .find { it.id == id }
            ?.toEntity()
            ?: throw ProjectNotFoundException("Project with id $id not found")
    }

    override suspend fun getAllProjects(): List<ProjectEntity> {
        return dataSource.loadAll().map { it.toEntity() }
    }

    override suspend fun deleteProject(id: UUID) {
        dataSource.loadAll()
            .find { it.id == id }
            ?.also { dataSource.delete(it) }
            ?: throw ProjectNotFoundException("Project with id $id not found")
    }

    override suspend fun editProject(project: ProjectEntity) {
        dataSource.update(project.toDto())
    }
}