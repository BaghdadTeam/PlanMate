package org.baghdad.logic.usecase.admin

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID
class IsAdminUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * Checks if user is admin (non-throwing version)
     * @return true if admin, false otherwise
     */
    suspend operator fun invoke(userId: UUID): Boolean {
        return try {
            ensureAdmin(userId)
            true
        } catch (e: UnauthorizedException) {
            false
        }
    }

    /**
     * Verifies admin status (throwing version)
     * @throws UnauthorizedException if user is not admin
     */
    suspend fun ensureAdmin(userId: UUID) {
        val user = userRepository.getUserById(userId)
            ?: throw UnauthorizedException("User not found")

        if (user.type != UserType.Admin) {
            throw UnauthorizedException("Only admins can perform this action.")
        }
    }
}