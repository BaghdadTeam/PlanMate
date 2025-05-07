package org.baghdad.data.local

import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import java.util.UUID

class ProjectDataSource(
    private val projectDataSource: DataSource<ProjectEntity>,
    private val projectStatesDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    fun createProject(project: ProjectEntity) {
        runBlocking {
            projectDataSource.append(project)
        }
    }

    fun getAllProjects(): List<ProjectEntity> {
        return runBlocking {
            projectDataSource.loadAll()
        }
    }

    fun getProjectById(id: String): ProjectEntity? {
        return runBlocking {
            projectDataSource.loadAll().find { it.id.toString() == id }
        }
    fun getProjectById(id: UUID): ProjectEntity {
        return projectDataSource.loadAll().find { it.id == id }
            .takeIf { it != null }?: throw ProjectNotFoundException("No project found")
    }

    fun updateProject(project: ProjectEntity) {
        runBlocking {
            projectDataSource.update(project)
        }
    }

    fun deleteProject(projectId: UUID) {
        runBlocking {
            val projects = projectDataSource.loadAll().toMutableList()
            val project = projects.find { it.id == projectId } ?: throw Exception("No project found")
        val projects = projectDataSource.loadAll().toMutableList()
        val project = projects.indexOfFirst { it.id == projectId }
        if (project == -1) throw ProjectNotFoundException("No project found")

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
}