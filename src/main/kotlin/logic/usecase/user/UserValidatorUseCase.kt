package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.*

class UserValidatorUseCase(
    private val userRepository: UserRepository,
    private val adminValidation: AdminPermissionCheckerUseCase
) {
    suspend operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creatorId: UUID,
        isAdmin: Boolean = false
    ) {

        validateUsername(username)
        adminValidation(creatorId)
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


    private fun validateName(name: String) {
        if (name.isBlank()) throw InvalidNameException("Name must not be empty.")
    }


    private fun validatePassword(password: String) {
        if (password.length < 6) throw InvalidPasswordException("Password must be at least 6 characters.")
    }

    private suspend fun ensureUsernameUnique(username: String) {
        val usernameExist = userRepository.isUsernameTaken(username)
        if (usernameExist) {
            throw UserAlreadyExistsException("User with username $username already exists.")
        }
    }


}