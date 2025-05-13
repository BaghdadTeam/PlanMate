package org.baghdad.logic.usecase.admin

import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class AdminPermissionCheckerUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(userId: UUID): Boolean {
        return validateAdminPermission(userId)

    }

    private suspend fun validateAdminPermission(userId: UUID): Boolean {
        val user = userRepository.getUserById(userId)
        return user.type == UserType.Admin
    }

}