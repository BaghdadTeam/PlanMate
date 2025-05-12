package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.user.UserDto
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import java.util.UUID

class UserDataSource(
    private val dataSource: DataSource<UserDto>
) {
    suspend fun loadUsers() = dataSource.loadAll()

    suspend fun addUser(user: UserEntity, hashedPassword: String) {
        dataSource.append(
            UserDto(
                id = user.id,
                username = user.username,
                hashedPassword = hashedPassword,
                type = user.type.toString(),
                name = user.name,
            )
        )
    }

    suspend fun findUserByUsername(username: String): UserDto {
        return loadUsers().firstOrNull() { it.username == username }
            ?: throw UserNotFoundException("User not found with username: $username")
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        return loadUsers().any { it.username == username }
    }

    suspend fun findUserById(id: UUID): UserDto? {
        return loadUsers().find { it.id == id }
    }
}
