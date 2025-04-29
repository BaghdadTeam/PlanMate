package org.baghdad.logic.model.entities

import java.util.UUID

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val stateId: String,
    val projectId: String,
    val creatorId: String
)