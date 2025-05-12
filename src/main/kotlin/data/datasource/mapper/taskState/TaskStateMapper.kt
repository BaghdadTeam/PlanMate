package org.baghdad.data.datasource.mapper.state

import data.datasource.mapper.taskState.TaskStateColumns
import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.TaskStateEntity
import java.util.UUID

class TaskStateMapper : CsvMapper<TaskStateEntity> {
    override fun header(): String {
        return "id,name,projectId,creatorId"
    }

    override fun deserializer(content: String): TaskStateEntity {
        val state = content.split(",")
        return TaskStateEntity(
            id = UUID.fromString(state[TaskStateColumns.ID]),
            name = state[TaskStateColumns.NAME],
            projectId = UUID.fromString(state[TaskStateColumns.PROJECT_ID]),
            creatorId = UUID.fromString(state[TaskStateColumns.CREATOR_ID]),
        )
    }

    override fun getId(item: TaskStateEntity): String {
        return item.id.toString()
    }

    override fun serializer(item: TaskStateEntity): String {
        return "${item.id},${item.name},${item.projectId},${item.creatorId}"
    }
}