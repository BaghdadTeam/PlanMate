package org.baghdad.data.datasource.mapper.state

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.StateEntity
import java.util.UUID

class StateMapper : CsvMapper<StateEntity> {
    override fun header(): String {
        return "id,name,projectId,creatorId"
    }

    override fun deserializer(content: String): StateEntity {
        val state = content.split(",")
        return StateEntity(
            id = UUID.fromString(state[StateColumns.ID]),
            name = state[StateColumns.NAME],
            projectId = UUID.fromString(state[StateColumns.PROJECT_ID]),
            creatorId = UUID.fromString(state[StateColumns.CREATOR_ID]),
        )
    }

    override fun serializer(item: StateEntity): String {
        return "${item.id},${item.name},${item.projectId},${item.creatorId}"
    }
}