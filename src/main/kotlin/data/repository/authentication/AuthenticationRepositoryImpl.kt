package org.baghdad.data.repository.authentication
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository


class AuthenticationRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val sessionRepository: SessionRepository
): AuthenticationRepository {
    override fun login(username: String, password: String): Result<SessionEntity>{
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

}