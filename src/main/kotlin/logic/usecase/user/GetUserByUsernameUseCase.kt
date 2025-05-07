package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.repositories.UserRepository

class GetUserByUsernameUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String): UserEntity {
        if (username.isBlank()) {
            throw InvalidUsernameException("Username must not be empty.")
        }
        return userRepository.getUserByUsername(username)
    }
}
