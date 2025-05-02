package org.baghdad.logic.usecase.user

import mu.KotlinLogging
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.md5WithSalt

private val logger = KotlinLogging.logger {}

class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        username: String,
        passwordPlain: String,
        name: String,
        creator: UserEntity
    ): CreateUserResult {
        logger.info { "CreateUser invoked by ${creator.username} for new user '$username'" }

        if (creator.type != UserType.Admin) {
            val msg = "Only admins can create users."
            logger.warn { msg }
            return CreateUserResult.Failure(msg)
        }
        if (userRepository.existsByUsername(username)) {
            val msg = "Username '$username' already exists."
            logger.warn { msg }
            return CreateUserResult.Failure(msg)
        }

        val hashed = passwordPlain.md5WithSalt()
        val newUser = UserEntity(name = name, username = username, hashedPassword = hashed, type = UserType.Mate)
        userRepository.createUser(newUser)
        logger.info { "User '${newUser.username}' created successfully." }
        return CreateUserResult.Success(newUser)
    }
}
