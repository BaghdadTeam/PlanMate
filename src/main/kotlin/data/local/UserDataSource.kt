package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.UserDto
import org.baghdad.logic.model.exceptions.UserNotFoundException
import java.util.*

class UserDataSource(
    private val dataSource: DataSource<UserDto>
) {
    suspend fun loadUsers() = dataSource.loadAll()

    suspend fun addUser(user: UserDto) {
        dataSource.append(user)
    }

    suspend fun findUserByUsername(username: String): UserDto {
        return loadUsers().firstOrNull() { it.username == username }
            ?: throw UserNotFoundException()
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        return loadUsers().any { it.username == username }
    }

    suspend fun findUserById(id: UUID): UserDto? {
        return loadUsers().find { it.id == id }
    }
}
