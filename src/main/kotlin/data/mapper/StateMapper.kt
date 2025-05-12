package org.baghdad.data.mapper

import org.baghdad.data.dto.StateDto
import org.baghdad.logic.model.entities.StateEntity

fun StateDto.toDomain(): StateEntity {
    return StateEntity(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}

fun StateEntity.toDto(): StateDto {
    return StateDto(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}