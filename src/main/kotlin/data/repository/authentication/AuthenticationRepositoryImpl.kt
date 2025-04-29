package org.baghdad.data.repository.authentication
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.AuthenticationRepository


class AuthenticationRepositoryImpl(val dataSource: UserDataSource): AuthenticationRepository {
    override fun login(username: String, password: String): Result<SessionEntity>{
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

}