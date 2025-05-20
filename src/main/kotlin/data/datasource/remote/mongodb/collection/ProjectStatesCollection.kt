package org.baghdad.data.datasource.remote.mongodb.collection

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.baghdad.data.dto.TaskStateDto
import java.util.*

class ProjectStatesCollection(
    private val collection: MongoCollection<TaskStateDto>,
    private val taskCollection: TaskCollection,
) {

    suspend fun createProjectState(projectState: TaskStateDto) {
        collection.insertOne(projectState)
    }

    suspend fun getAllProjectStatesByProjectId(projectId: UUID): List<TaskStateDto> {
        val filter = Filters.eq("projectId", projectId)
        return collection.find(filter).toList()
    }

    suspend fun deleteProjectState(id: UUID) {
        val filter = Filters.eq("_id", id)
        collection.deleteOne(filter)
        val tasks = taskCollection.getAllTasksByProjectStateId(id)
        tasks.forEach {
            taskCollection.deleteTask(it.id)
        }
    }
}