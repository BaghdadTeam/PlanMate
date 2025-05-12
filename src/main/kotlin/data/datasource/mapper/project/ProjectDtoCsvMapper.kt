package org.baghdad.data.datasource.mapper.project

import org.baghdad.data.dto.project.ProjectDto
import org.baghdad.data.datasource.CsvMapper
import java.util.UUID

class ProjectDtoCsvMapper : CsvMapper<ProjectDto> {

    override fun header() = "id,name,creatorId"

    override fun serializer(item: ProjectDto) = "${item.id},${item.name},${item.creatorId}"

    override fun deserializer(content: String): ProjectDto {
        val project = content.split(",")
        return ProjectDto(
            id = UUID.fromString(project[ProjectColumns.ID]),
            name = project[ProjectColumns.NAME],
            creatorId = UUID.fromString(project[ProjectColumns.CREATOR_ID])
        )
    }

    override fun getId(item: ProjectDto) = item.id.toString()
}
