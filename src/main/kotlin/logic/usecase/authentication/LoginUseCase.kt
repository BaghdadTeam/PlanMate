package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.repositories.TokenProvider
import org.baghdad.utils.md5WithSalt
import java.time.LocalDateTime

class LoginUseCase (
    private val authRepository: AuthenticationRepository,
    private val sessionRepository: SessionRepository,
    private val tokenProvider: TokenProvider
) {
    operator fun invoke(username: String, password: String): Result<SessionEntity> {

        if (username.isBlank())
            return Result.failure(InvalidCredentialsException("username must not be blank"))
        if (password.isBlank())
            return Result.failure(InvalidCredentialsException("Password must not be empty"))
        val result = authRepository.login(username, password.md5WithSalt())
        return result.map (::createAndSaveSession)
    }

    private fun createAndSaveSession(user: UserEntity): SessionEntity {
        return SessionEntity(
            userId = user.id,
            token = tokenProvider.generateToken(),
            loginTime = LocalDateTime.now()
        ).also {
            sessionRepository.saveSession(it)
        }
    }
}