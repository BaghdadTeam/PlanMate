package org.baghdad.data.repositories.project

import data.datasource.local.csv.files.ProjectDataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.*

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
