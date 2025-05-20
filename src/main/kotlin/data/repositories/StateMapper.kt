package org.baghdad.data.repositories

import org.baghdad.data.dto.TaskStateDto
import org.baghdad.logic.model.entities.TaskStateEntity

fun TaskStateDto.toDomain(): TaskStateEntity {
    return TaskStateEntity(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}

fun TaskStateEntity.toDto(): TaskStateDto {
    return TaskStateDto(
        id = id,
        name = name,
        projectId = projectId,
        creatorId = creatorId
    )
}