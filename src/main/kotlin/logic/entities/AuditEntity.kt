package org.baghdad.logic.entities

data class AuditEntity(
    val id: String,
    val entityType: String,
    val entityId: String,
    val action: String,
    val performedByUserId: String,
    val timestamp: String
)