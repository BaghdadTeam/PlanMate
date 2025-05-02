package org.baghdad.logic.usecase.user

import mu.KotlinLogging
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository

private val logger = KotlinLogging.logger {}

class GetUserByUsernameUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String): GetUserResult {
        logger.info { "Looking up user '$username'" }
        val user = userRepository.findByUsername(username)
        return if (user != null) {
            logger.info { "User '$username' found" }
            GetUserResult.Success(user)
        } else {
            logger.warn { "User '$username' not found" }
            GetUserResult.NotFound
        }
    }
}
