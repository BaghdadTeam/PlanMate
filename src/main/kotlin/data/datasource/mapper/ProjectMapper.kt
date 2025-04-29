package org.baghdad.data.datasource.mapper

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.ProjectEntity
import java.util.UUID

class ProjectMapper: CsvMapper<ProjectEntity> {
    override fun header(): String {
        return "id,name,creatorId"
    }

    override fun deserializer(content: String): ProjectEntity {
        val project = content.split(",")
        return ProjectEntity(
            id = UUID.fromString(project[ProjectColumns.ID]),
            name = project[ProjectColumns.NAME],
            creatorId = project[ProjectColumns.CREATOR_ID]
        )
    }

    override fun serializer(item: ProjectEntity): String {
        return "${item.id},${item.name},${item.creatorId}"
    }
}