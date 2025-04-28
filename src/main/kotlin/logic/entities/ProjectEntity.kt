package org.baghdad.logic.entities

import java.util.UUID

data class ProjectEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val creatorId: String
)
