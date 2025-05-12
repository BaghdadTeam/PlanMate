package org.baghdad.data.mapper

import org.baghdad.data.dto.TaskStateDto
import org.baghdad.logic.model.entities.StateEntity

fun TaskStateDto.toDomain(): StateEntity {
    return StateEntity(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}

fun StateEntity.toDto(): TaskStateDto {
    return TaskStateDto(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}