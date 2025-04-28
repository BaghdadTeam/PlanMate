package org.baghdad.logic.entities

import java.util.UUID

data class AuditEntity(
    val id: UUID = UUID.randomUUID(),
    val entityType: String,  // => Project | State | Task
    val entityId: String,   // => Project | State | Task
    val action: String,
    val user: UserEntity,
    val timestamp: String
)