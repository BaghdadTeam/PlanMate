package data.datasource.mapper.taskState

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.dto.StateDto
import java.util.UUID

class TaskStateMapper : CsvMapper<StateDto> {
    override fun header(): String {
        return "id,name,projectId,creatorId"
    }

    override fun deserializer(content: String): StateDto {
        val state = content.split(",")
        return StateDto(
            id = UUID.fromString(state[TaskStateColumns.ID]),
            name = state[TaskStateColumns.NAME],
            projectId = UUID.fromString(state[TaskStateColumns.PROJECT_ID]),
            creatorId = UUID.fromString(state[TaskStateColumns.CREATOR_ID]),
        )
    }

    override fun getId(item: StateDto): String {
        return item.id.toString()
    }

    override fun serializer(item: StateDto): String {
        return "${item.id},${item.name},${item.projectId},${item.creatorId}"
    }
}