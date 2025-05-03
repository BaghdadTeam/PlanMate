package org.baghdad.logic.usecase.user

import mu.KotlinLogging
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.utils.md5WithSalt

private val logger = KotlinLogging.logger {}

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creator: UserEntity
    ): Result<UserEntity> {
        logger.info { "CreateUser invoked by ${creator.username} for new user '$username'" }

        return runCatching {
            checkAdmin(creator)
            ensureUsernameUnique(username)

            val newUser = createUserEntity(username, passwordPlain, name)
            userRepository.createUser(newUser)

            logger.info { "User '${newUser.username}' created successfully." }
            newUser
        }
    }

    private fun checkAdmin(user: UserEntity) {
        if (user.type != UserType.Admin) {
            throw UnauthorizedException("Only admins can create users.")
        }
    }

    private fun ensureUsernameUnique(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UserAlreadyExistsException("Username '$username' already exists.")
        }
    }

    private fun createUserEntity(username: String, password: String, name: String): UserEntity {
        return UserEntity(
            name = name,
            username = username,
            hashedPassword = password.md5WithSalt(),
            type = UserType.Mate
        )
    }
}
