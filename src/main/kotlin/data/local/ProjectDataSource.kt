package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.project.ProjectDto
import org.baghdad.data.mapper.toDto
import org.baghdad.data.mapper.toEntity
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import java.util.*

class ProjectDataSource(
    private val projectDataSource: DataSource<ProjectDto>,
    private val projectStatesDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    suspend fun createProject(project: ProjectEntity) {
        projectDataSource.append(project.toDto())
    }

    suspend fun getAllProjects(): List<ProjectEntity> {
        return projectDataSource.loadAll().map { it.toEntity() }
    }

    suspend fun getProjectById(id: UUID): ProjectEntity {
        return projectDataSource.loadAll().find { it.id == id }
            ?.toEntity()
            ?: throw ProjectNotFoundException("Project with id $id not found")
    }

    suspend fun updateProject(project: ProjectEntity) {
        val projects = projectDataSource.loadAll()
        if (projects.none { it.id == project.id }) {
            throw ProjectNotFoundException("Project with id ${project.id} not found")
        }
        projectDataSource.update(project.toDto())
    }

    suspend fun deleteProject(projectId: UUID) {
        val projects = projectDataSource.loadAll().toMutableList()
        val project = projects.find { it.id == projectId }
            ?: throw ProjectNotFoundException("Project with id $projectId not found")

        val projectStates = projectStatesDataSource.loadAll().toMutableList()
        val tasks = taskDataSource.loadAll().toMutableList()

        val filteredProjectStates = projectStates.filter { it.projectId == projectId }
        val filteredTasks = tasks.filter { it.projectId == projectId }

        projectDataSource.delete(project)
        filteredProjectStates.forEach {
            projectStatesDataSource.delete(it)
        }
        filteredTasks.forEach {
            taskDataSource.delete(it)
        }
    }
}