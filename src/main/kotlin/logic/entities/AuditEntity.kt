package org.baghdad.logic.entities

import java.util.UUID

data class AuditEntity(
    val id: UUID = UUID.randomUUID(),
    val entityType: String,
    val entityId: String,
    val action: String,
    val performedByUserId: String,
    val timestamp: String
)