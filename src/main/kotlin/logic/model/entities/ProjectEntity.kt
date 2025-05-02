package org.baghdad.logic.model.entities

import java.util.UUID

data class ProjectEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val creatorId: UUID = UUID.randomUUID()
)