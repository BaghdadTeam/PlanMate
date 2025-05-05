package org.baghdad.data.repositories.authentication
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository


class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val sessionRepository: SessionRepository
): AuthenticationRepository {

    override fun login(username: String, inputHashedPassword: String):UserEntity {
        val user = userDataSource.findUserByUsername(username)
        if (inputHashedPassword != user.hashedPassword)
            throw InvalidPasswordException("Invalid hashedPassword")
        return user
    }

    override fun logout() {
        return if (sessionRepository.deleteSession()) {
        } else {
            throw LogoutFailedException("Logout failed")
        }
    }
}