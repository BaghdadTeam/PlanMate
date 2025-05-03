package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(id: UUID): Result<UserEntity> {
        return try {
            Result.success(repository.getUserById(id))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
