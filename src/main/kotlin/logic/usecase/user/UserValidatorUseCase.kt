package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class UserValidatorUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
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
            throw InvalidUsernameException("Username must not be empty.")
        }
        val re = Regex("^[A-Za-z0-9_]{3,20}\$")
        if (!username.matches(re)) {
            throw InvalidUsernameException(
                "Username must be 3–20 chars, letters, digits or underscore only."
            )
        }
    }

    private fun checkAdmin(userId: UUID) {
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) {
            throw UnauthorizedException("Only admins can create users.")
        }
    }

    private fun validateName(name: String) {
        if (name.isBlank()) throw InvalidNameException("Name must not be empty.")
    }


    private fun validatePassword(password: String) {
        if (password.length < 6) throw InvalidPasswordException("Password must be at least 6 characters.")
    }

    private fun ensureUsernameUnique(username: String) {
        try {
            userRepository.getUserByUsername(username)
            throw UserAlreadyExistsException("Username '$username' already exists.")
        } catch (exception: UserNotFoundException) {
        }
    }
}