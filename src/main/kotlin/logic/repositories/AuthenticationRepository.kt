package org.baghdad.logic.repositories
import org.baghdad.logic.model.entities.UserEntity

interface AuthenticationRepository {

    fun login(username: String, inputHashedPassword: String): Result<UserEntity>
    fun logout(): Result<Unit>

}