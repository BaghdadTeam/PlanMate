package org.baghdad.logic.usecase.admin

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class IsAdminUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: UUID): Boolean {
        return try {
            checkAdmin(userId)
            true
        } catch (e: UnauthorizedException) {
            false
        }
    }

    suspend fun ensureAdmin(userId: UUID) {
        checkAdmin(userId)
    }

    private suspend fun checkAdmin(userId: UUID) {
        val user = userRepository.getUserById(userId)
            ?: throw UnauthorizedException("User not found.")

        if (user.type != UserType.Admin) {
            throw UnauthorizedException("Only admins can perform this action.")
        }
    }
}