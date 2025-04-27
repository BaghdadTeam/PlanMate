package org.baghdad.logic.entities

import java.util.UUID

data class UserEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val userName: String,
    val hashedPassword: String,
    val type: UserType
)