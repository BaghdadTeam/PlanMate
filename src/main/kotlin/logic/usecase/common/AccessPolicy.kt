package org.baghdad.logic.usecase.common

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType

object AccessPolicy {
    fun requireAdmin(user: UserEntity): Result<Unit> {
        return if (user.type == UserType.Admin) {
            Result.Success(Unit)
        } else {
            throw IllegalAccessException("Only Admins can perform this action!.")
        }
    }
}