package org.baghdad.data.repositories.user

import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override fun createUser(user: UserEntity) {
        dataSource.addUser(user)
    }

    override fun getUserByUsername(username: String): UserEntity {
        return dataSource.findUserByUsername(username)
    }

    override fun getUserById(id: UUID): UserEntity {
        return dataSource.findUserById(id)
            ?: throw UserNotFoundException("User not found with id: $id")
    }

    override fun getAllUsers(): List<UserEntity> = dataSource.loadUsers()
}
