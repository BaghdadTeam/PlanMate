package org.baghdad.logic.usecase.admin

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class AdminPermissionCheckerUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * Checks if user is admin
     * @return true if admin, false otherwise
     */
    suspend operator fun invoke(userId: UUID): Boolean {
        requireNotNull(userId) { "User not found" }
        return try {
            validateAdminPermission(userId)
            true
        } catch (e: UnauthorizedException) {
            false
        }
    }

    /**
     * Verifies admin status
     * @throws UnauthorizedException if user is not admin
     */
    suspend fun validateAdminPermission(userId: UUID) {
        val user = userRepository.getUserById(userId) ?: null
        if (user?.type != UserType.Admin) {
            throw UnauthorizedException(ADMIN_ONLY_MESSAGE)
        }
    }

    private companion object {
        const val ADMIN_ONLY_MESSAGE = "Only admins can perform this action."
    }
}