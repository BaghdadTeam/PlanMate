package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.ProjectEntity
import java.util.UUID

interface ProjectRepository {

    fun createProject(project: ProjectEntity)
    fun getProjectById(id: UUID): ProjectEntity?
    fun getAllProjects(): List<ProjectEntity>
    fun deleteProject(id: UUID)
    fun editProject(project: ProjectEntity)
}