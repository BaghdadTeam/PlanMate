package org.baghdad.data.datasource.mapper

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.TaskEntity
import java.util.*

class TaskMapper : CsvMapper<TaskEntity> {
    override fun header(): String {
        return "id,title,description,stateId,projectId,creatorId"
    }

    override fun deserializer(content: String): TaskEntity {
        val task = content.split(",")
        return TaskEntity(
            id = UUID.fromString(task[TaskColumns.ID]),
            title = task[TaskColumns.TITLE],
            description = task[TaskColumns.DESCRIPTION],
            stateId = task[TaskColumns.STATE_ID],
            projectId = task[TaskColumns.PROJECT_ID],
            creatorId = task[TaskColumns.CREATOR_ID],
        )
    }

    override fun serializer(item: TaskEntity): String {
        return "${item.id},${item.title},${item.description},${item.stateId},${item.projectId},${item.creatorId}"
    }
}