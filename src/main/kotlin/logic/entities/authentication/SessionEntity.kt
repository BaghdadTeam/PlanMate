package org.baghdad.logic.entities.authentication

import kotlinx.datetime.LocalTime
import org.baghdad.logic.entities.UserEntity

data class SessionEntity(
    val id: String,
    val userData: UserEntity,
    val token: String,
    val loginTime: LocalTime,
)
