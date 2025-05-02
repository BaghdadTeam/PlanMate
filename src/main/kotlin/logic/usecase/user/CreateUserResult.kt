package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity

sealed class CreateUserResult {
    data class Success(val user: UserEntity) : CreateUserResult()
    data class Failure(val error: String) : CreateUserResult()
}