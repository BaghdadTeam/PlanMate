package org.baghdad.data.datasource.mapper.project

import org.baghdad.data.dto.ProjectDto
import org.baghdad.data.datasource.CsvMapper
import java.util.UUID

class ProjectDtoCsvMapper : CsvMapper<ProjectDto> {
    override fun header() = "id,name,creatorId"
    override fun serializer(item: ProjectDto) =
        "${item.id},${item.name},${item.creatorId}"
    override fun deserializer(content: String): ProjectDto {
        val parts = content.split(",")
        return ProjectDto(
            id        = UUID.fromString(parts[0]),
            name      = parts[1],
            creatorId = UUID.fromString(parts[2])
        )
    }
    override fun getId(item: ProjectDto) = item.id.toString()
}
