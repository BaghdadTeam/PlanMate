package org.baghdad.logic.usecase.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.InvalidUsernameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository

class GetUserByUsernameUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(username: String): UserEntity {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        if (username.isBlank()) {
            throw InvalidUsernameException()
        }
        return userRepository.getUserByUsername(username)
    }
}
