package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetUserByUserIdUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: UUID): UserEntity {
        return userRepository.getUserById(userId)
    }
}
