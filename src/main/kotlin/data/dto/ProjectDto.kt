package org.baghdad.data.dto

import java.util.UUID

data class ProjectDto(
    val id: UUID,
    val name: String,
    val creatorId: UUID
)
