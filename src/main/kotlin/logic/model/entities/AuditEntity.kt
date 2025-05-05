package org.baghdad.logic.model.entities

import java.util.UUID

data class AuditEntity(
    val id: UUID = UUID.randomUUID(),
    val entityType: String,  // => Project | State | Task
    val entityId: UUID,   // => Project | State | Task
    val action: String,
    val user: UserEntity,
    val timestamp: String
)