package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import java.util.UUID

class UserDataSource(
    private val dataSource: DataSource<UserEntity>
) {
    fun loadUsers() = dataSource.loadAll()

    fun addUser(user: UserEntity) {
        dataSource.append(user)
    }

    fun findUserByUsername(username: String): UserEntity {
        return loadUsers().firstOrNull { it.username == username }
            ?: throw UserNotFoundException("User not found with username: $username")
    }

    fun findUserById(id: UUID): UserEntity? {
        return loadUsers().find { it.id == id }
    }
}