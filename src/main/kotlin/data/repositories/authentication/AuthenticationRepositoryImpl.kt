package org.baghdad.data.repositories.authentication
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository


class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val sessionRepository: SessionRepository
): AuthenticationRepository {
    override fun login(username: String, password: String): Result<UserEntity>{
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

}