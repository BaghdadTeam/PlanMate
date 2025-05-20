package org.baghdad.data.datasource.remote.mongodb.collection

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.baghdad.data.dto.ProjectDto
import org.baghdad.logic.model.exceptions.ProjectNotFoundException
import java.util.UUID

class ProjectCollection(
    private val projectStatesCollection: ProjectStatesCollection,
    private val taskCollection: TaskCollection,
    private val projectCollection: MongoCollection<ProjectDto>
) {

    suspend fun createProject(project: ProjectDto) {
        projectCollection.insertOne(project)
    }

    suspend fun getAllProjects(): List<ProjectDto> {
        return projectCollection.find().toList()
    }

    suspend fun getProjectById(id: String): ProjectDto {
        val filter = Filters.eq("_id", id)
        return projectCollection.find(filter).toList().firstOrNull()
            ?: throw ProjectNotFoundException("Project with id $id not found")
    }

    suspend fun updateProject(project: ProjectDto) {
        val filter = Filters.eq("_id", project.id)
        projectCollection.findOneAndReplace(filter, project)
    }

    suspend fun deleteProject(projectId: UUID) {
        val filter = Filters.eq("_id", projectId)

        val projectStates = projectStatesCollection.getAllProjectStatesByProjectId(projectId)
        val tasks = taskCollection.getAllTasksByProjectId(projectId)

        projectCollection.deleteOne(filter)
        projectStates.forEach {
            projectStatesCollection.deleteProjectState(it.id)
        }
        tasks.forEach {
            taskCollection.deleteTask(it.id)
        }
    }
}