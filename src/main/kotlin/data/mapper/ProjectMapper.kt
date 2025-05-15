package org.baghdad.data.mapper

import org.baghdad.data.dto.project.ProjectDto
import org.baghdad.logic.model.entities.ProjectEntity

fun ProjectDto.toEntity() = ProjectEntity(
    id = this.id,
    name = this.name,
    creatorId = this.creatorId
)

fun ProjectEntity.toDto() = ProjectDto(
    id = this.id,
    name = this.name,
    creatorId = this.creatorId
)
