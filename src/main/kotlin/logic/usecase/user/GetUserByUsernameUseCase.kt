package org.baghdad.logic.usecase.user

import mu.KotlinLogging
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository

private val logger = KotlinLogging.logger {}

class GetUserByUsernameUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String): Result<UserEntity> {
        logger.info { "Looking up user '$username'" }

        return runCatching {
            val user = userRepository.findByUsername(username)
                ?: throw NoSuchElementException("User '$username' not found.")
            logger.info { "User '$username' found" }
            user
        }
    }
}
