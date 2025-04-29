package org.baghdad.logic.entities.authentication


import org.baghdad.logic.entities.UserEntity
import java.time.LocalDateTime
import java.util.UUID

data class SessionEntity(
    val id: UUID = UUID.randomUUID(),
    val userData: UserEntity,
    val token: String,
    val loginTime: LocalDateTime
)
