package org.baghdad.logic.usecases.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository

class LoginUseCase (
    private val authRepository: AuthenticationRepository,
    private val sessionRepository: SessionRepository
)
{
    operator fun invoke(username: String, password: String): Result<SessionEntity> {
        TODO()
    }
}