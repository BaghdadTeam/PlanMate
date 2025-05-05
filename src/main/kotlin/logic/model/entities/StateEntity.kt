package org.baghdad.logic.model.entities

import java.util.UUID

data class StateEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val projectId: UUID,
    val creatorId: UUID
)
