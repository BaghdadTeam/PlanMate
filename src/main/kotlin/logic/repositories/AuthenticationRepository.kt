package org.baghdad.logic.repositories
import org.baghdad.logic.model.entities.UserEntity

interface AuthenticationRepository {

    suspend fun login(username: String, inputHashedPassword: String): UserEntity
    suspend fun logout()

}