package data.datasource.mapper.taskState

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.mapper.taskState.TaskStateColumns
import org.baghdad.data.dto.TaskStateDto
import java.util.UUID

class TaskStateMapper : CsvMapper<TaskStateDto> {
    override fun header(): String {
        return "id,name,projectId,creatorId"
    }

    override fun deserializer(content: String): TaskStateDto {
        val state = content.split(",")
        return TaskStateDto(
            id = UUID.fromString(state[TaskStateColumns.ID]),
            name = state[TaskStateColumns.NAME],
            projectId = UUID.fromString(state[TaskStateColumns.PROJECT_ID]),
            creatorId = UUID.fromString(state[TaskStateColumns.CREATOR_ID]),
        )
    }

    override fun getId(item: TaskStateDto): String {
        return item.id.toString()
    }

    override fun serializer(item: TaskStateDto): String {
        return "${item.id},${item.name},${item.projectId},${item.creatorId}"
    }
}