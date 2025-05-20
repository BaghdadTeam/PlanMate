package org.baghdad.data.datasource.remote.mongodb.collection

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.baghdad.logic.model.entities.TaskEntity
import java.util.*

class TaskCollection(
    private val collection: MongoCollection<TaskEntity>
) {
    suspend fun createTask(task: TaskEntity) {
        collection.insertOne(task)
    }

    suspend fun getAllTasksByProjectId(projectId: UUID): List<TaskEntity> {
        val filter = Filters.eq("projectId", projectId)
        return collection.find(filter).toList()
    }

    suspend fun getAllTasksByProjectStateId(projectStateId: UUID): List<TaskEntity> {
        val filter = Filters.eq("stateId", projectStateId)
        return collection.find(filter).toList()
    }

    suspend fun deleteTask(taskId: UUID) {
        val filter = Filters.eq("_id", taskId)
        collection.deleteOne(filter)
    }
}