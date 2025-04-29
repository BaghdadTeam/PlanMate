package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.ProjectEntity

interface ProjectRepository {

    fun createProject(project: ProjectEntity)
    fun getProjectById(id: String): ProjectEntity?
    fun getAllProjects(): List<ProjectEntity>
    fun deleteProject(id: String): Boolean
}