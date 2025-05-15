package org.baghdad.data.repositories.authentication

import org.baghdad.data.local.SessionDataSource
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repositories.toDomain
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.repositories.AuthenticationRepository


class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val sessionDataSource: SessionDataSource
) : AuthenticationRepository {

    override suspend fun login(username: String, inputHashedPassword: String): UserEntity {
        val user = userDataSource.findUserByUsername(username)
        if (inputHashedPassword != user.hashedPassword)
            throw InvalidPasswordException("Invalid hashedPassword")
        return user.toDomain()
    }

    override suspend fun logout() {
        return if (sessionDataSource.deleteSession()) {
        } else {
            throw LogoutFailedException("Logout failed")
        }
    }
}