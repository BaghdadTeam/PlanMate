package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.exceptions.InvalidNameException
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.InvalidUsernameException
import org.baghdad.logic.model.exceptions.UserAlreadyExistsException
import org.baghdad.logic.repositories.UserRepository

class UserValidatorUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
    ) {

        validateUsername(username)
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