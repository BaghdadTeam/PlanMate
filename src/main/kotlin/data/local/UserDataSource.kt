package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import java.util.UUID

class UserDataSource(
    private val dataSource: DataSource<UserEntity>
) {
    suspend fun loadUsers() = dataSource.loadAll()

    suspend fun addUser(user: UserEntity) {
        dataSource.append(user)
    }

    suspend fun findUserByUsername(username: String): UserEntity {
        return loadUsers().firstOrNull() { it.username == username }
            ?: throw UserNotFoundException("User not found with username: $username")
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        return loadUsers().any { it.username == username }
    }

    suspend fun findUserById(id: UUID): UserEntity? {
        return loadUsers().find { it.id == id }
    }
}
