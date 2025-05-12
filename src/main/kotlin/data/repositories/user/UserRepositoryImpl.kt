package org.baghdad.data.repositories.user

import org.baghdad.data.dto.user.toDomain
import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun createUser(user: UserEntity, hashedPassword: String) {
        dataSource.addUser(user, hashedPassword)
    }

    override suspend fun getUserByUsername(username: String): UserEntity {
        return dataSource.findUserByUsername(username).toDomain()
    }

    override suspend fun getUserById(id: UUID): UserEntity {
        return dataSource.findUserById(id)?.toDomain()
            ?: throw UserNotFoundException("User not found with id: $id")
    }

    override suspend fun isUsernameTaken(username: String): Boolean {
        return dataSource.isUsernameTaken(username)
    }

    override suspend fun getAllUsers(): List<UserEntity> = dataSource.loadUsers().map { it.toDomain() }
}
