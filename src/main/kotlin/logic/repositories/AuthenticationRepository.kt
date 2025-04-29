package org.baghdad.logic.repositories
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.UserEntity

interface AuthenticationRepository {

    fun login(username: String, password: String): Result<UserEntity>
    fun logout(): Result<Unit>

}