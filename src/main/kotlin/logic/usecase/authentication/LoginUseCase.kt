package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.repositories.TokenProvider
import org.baghdad.logic.utils.md5WithSalt
import java.time.LocalDateTime

class LoginUseCase(
    private val authRepository: AuthenticationRepository,
    private val sessionRepository: SessionRepository,
    private val tokenProvider: TokenProvider
) {
    suspend operator fun invoke(username: String, password: String): SessionEntity {

        if (username.isBlank())
            throw InvalidCredentialsException("username must not be blank")
        if (password.isBlank())
            throw InvalidCredentialsException("Password must not be empty")
        val userInfo = authRepository.login(username, password.md5WithSalt())
        return createAndSaveSession(userInfo)
    }

    private suspend fun createAndSaveSession(user: UserEntity): SessionEntity {
        return SessionEntity(
            userId = user.id,
            token = tokenProvider.generateToken(),
            loginTime = LocalDateTime.now()
        ).also {
            sessionRepository.saveSession(it)
        }
    }
}