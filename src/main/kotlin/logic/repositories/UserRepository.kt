package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.UserEntity
import java.util.UUID

interface UserRepository {

    suspend fun createUser(user: UserEntity, hashedPassword: String)
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun getUserByUsername(username: String): UserEntity
    suspend fun getUserById(id: UUID): UserEntity
    suspend fun isUsernameTaken(username: String): Boolean
}
