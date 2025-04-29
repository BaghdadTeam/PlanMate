package org.baghdad.logic.repositories.authentication

import org.baghdad.data.storage.user.UserStorage
import org.baghdad.logic.entities.authentication.SessionEntity
import org.baghdad.logic.repositories.AuthenticationRepository

class AuthenticationRepositoryImpl
    (
            private val sessionRepository: SessionRepository,
            private val userStorage: UserStorage
            ): AuthenticationRepository {

    override fun login(
        username: String,
        password: String
    ): Result<SessionEntity> {
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

}