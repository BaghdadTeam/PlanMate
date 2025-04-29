package org.baghdad.logic.model.entities
import java.time.LocalDateTime
import java.util.UUID

data class SessionEntity(
    val id: UUID = UUID.randomUUID(),
    val userId: String,
    val token: String,
    val loginTime: LocalDateTime,
)
