package org.baghdad.logic.entities

import java.util.UUID

data class StateEntity(
    val id: UUID,
    val name: String,
    val projectId: String
)
