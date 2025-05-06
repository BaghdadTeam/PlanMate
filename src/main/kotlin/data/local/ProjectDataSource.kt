package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import java.util.UUID

class ProjectDataSource(
    private val projectDataSource: DataSource<ProjectEntity>,
    private val projectStatesDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    fun createProject(project: ProjectEntity) {
        projectDataSource.append(project)
    }

    fun getAllProjects(): List<ProjectEntity> {
        return projectDataSource.loadAll()
    }

    fun getProjectById(id: UUID): ProjectEntity? {
        return projectDataSource.loadAll().find { it.id == id }
    }

    fun updateProject(project: ProjectEntity) {
        val allData = projectDataSource.loadAll().toMutableList()
        val projectIndex = allData.indexOfFirst { it.id == project.id }
        if (projectIndex == -1) throw Exception("No project found")
        allData[projectIndex] = project
        projectDataSource.update(allData)
    }

    fun deleteProject(projectId: UUID) {
        val projects = projectDataSource.loadAll().toMutableList()
        val project = projects.indexOfFirst { it.id == projectId }
        if (project == -1) throw Exception("No project found")

        val projectStates = projectStatesDataSource.loadAll().toMutableList()
        val tasks = taskDataSource.loadAll().toMutableList()

        val filteredProjectStates = projectStates.filterNot { it.projectId == projectId }
        val filteredTasks = tasks.filterNot { it.projectId == projectId }

        projects.removeAt(project)

        projectDataSource.update(projects)
        projectStatesDataSource.update(filteredProjectStates)
        taskDataSource.update(filteredTasks)
    }
}