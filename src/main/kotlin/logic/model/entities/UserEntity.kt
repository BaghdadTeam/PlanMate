package org.baghdad.logic.model.entities

import java.util.*

data class UserEntity(
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val username: String,
    val type: UserType
): Identifiable

enum class UserType {
    Admin, Mate
}