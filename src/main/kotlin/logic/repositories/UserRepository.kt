package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.UserEntity
import java.util.UUID

interface UserRepository {

    fun createUser(username: String, userCreator: UserEntity)
    fun getUserByUsername(username: String): UserEntity?
    fun getUserById(id: UUID): UserEntity
    fun getAllUsers(): List<UserEntity>
}