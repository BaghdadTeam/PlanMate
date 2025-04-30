package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import java.util.UUID

class ProjectDataSource(
    private val projectDataSource: DataSource<ProjectEntity>,
    private val stateDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    fun createProject(project: ProjectEntity) {
        projectDataSource.append(project)
    }

    fun getProjects(): List<ProjectEntity> {
        return projectDataSource.loadAll().takeIf { it.isNotEmpty() }
            ?: throw ProjectNotFoundException("There is no available projects")
    }

    fun getProjectById(projectId: UUID): ProjectEntity {

        return projectDataSource.loadAll().firstOrNull { it.id == projectId }
            ?: throw ProjectNotFoundException("There is no available project for this ID: $projectId")
    }

    fun editProject(project: ProjectEntity) {
        val projects = getProjects().toMutableList()
        val projectIndex = projects.indexOfFirst { it.id == project.id }

        if (projectIndex != -1) {
            projects[projectIndex] = project

            projectDataSource.update(projects)
        } else {
            throw ProjectNotFoundException("There is no available project to be modified")
        }
    }

    fun deleteProject(projectId: UUID) {
        val projects = getProjects().toMutableList()
        val projectIndex = projects.indexOfFirst { it.id == projectId }

        if (projectIndex != -1) {
            val states = stateDataSource.loadAll()
            val newStates = states.filterNot { it.projectId == projectId.toString() }

            val tasks = taskDataSource.loadAll()
            val newTasks = tasks.filterNot { it.projectId == projectId.toString() }

            projects.removeAt(projectIndex)

            projectDataSource.update(projects)
            stateDataSource.update(newStates)
            taskDataSource.update(newTasks)

        } else {
            throw ProjectNotFoundException("There is no available project to be deleted")
        }
    }
}