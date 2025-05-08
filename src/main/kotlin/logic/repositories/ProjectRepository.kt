package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.ProjectEntity
import java.util.UUID

interface ProjectRepository {

    suspend fun createProject(project: ProjectEntity)
    suspend fun getProjectById(id: UUID): ProjectEntity
    suspend fun getAllProjects(): List<ProjectEntity>
    suspend fun deleteProject(id: UUID)
    suspend fun editProject(project: ProjectEntity)
}