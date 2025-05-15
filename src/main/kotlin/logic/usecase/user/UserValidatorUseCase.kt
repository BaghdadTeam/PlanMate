package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.*
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class UserValidatorUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creatorId: UUID
    ) {

        validateUsername(username)
        checkAdmin(creatorId)
        validateName(name)
        validatePassword(passwordPlain)
        ensureUsernameUnique(username)

    }


    private fun validateUsername(username: String) {
        if (username.isBlank()) {
            throw InvalidUsernameException()
        }
        val re = Regex("^[A-Za-z0-9_]{3,20}\$")
        if (!username.matches(re)) {
            throw InvalidUsernameException()
        }
    }

    private suspend fun checkAdmin(userId: UUID) {
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) {
            throw AccessDeniedException()
        }
    }

    private fun validateName(name: String) {
        if (name.isBlank()) throw InvalidNameException()
    }


    private fun validatePassword(password: String) {
        if (password.length < 6) throw InvalidPasswordException("Password must be at least 6 characters.")
    }

    private suspend fun ensureUsernameUnique(username: String) {
        val usernameExist = userRepository.isUsernameTaken(username)
        if (usernameExist) {
            throw UserAlreadyExistsException()
        }
    }
}