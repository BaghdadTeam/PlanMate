package org.baghdad.logic.usecase.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetUserByUserIdUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(userId: UUID): UserEntity {
        if (!sessionManager.isAuthenticated())  throw UnauthorizedException(" Not authenticated")
        return userRepository.getUserById(userId)
    }
}
