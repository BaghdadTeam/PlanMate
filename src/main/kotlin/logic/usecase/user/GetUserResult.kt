package org.baghdad.logic.usecase.user

import org.baghdad.logic.model.entities.UserEntity

sealed class GetUserResult {
    data class Success(val user: UserEntity) : GetUserResult()
    object NotFound : GetUserResult()
}
