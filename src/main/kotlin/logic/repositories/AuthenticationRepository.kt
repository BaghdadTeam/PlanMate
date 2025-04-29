package org.baghdad.logic.repositories

import org.baghdad.logic.entities.authentication.SessionEntity

interface AuthenticationRepository {
    fun login(username: String, password: String): Result<SessionEntity>
    fun logout() : Result<Unit>
}