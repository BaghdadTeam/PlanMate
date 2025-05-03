package org.baghdad.data.repositories.user

import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override fun createUser(user: UserEntity) = dataSource.addUser(user)

    override fun findByUsername(username: String): UserEntity? =
        try {
            dataSource.findUserByUsername(username)
        } catch (_: Exception) {
            null
        }

    override fun getUserById(id: UUID): UserEntity {
        return dataSource.findUserById(id)
            ?: throw UserNotFoundException("User with id $id not found")
    }



    override fun existsByUsername(username: String): Boolean =
        dataSource.existsByUsername(username)

    override fun getAllUsers(): List<UserEntity> = dataSource.loadUsers()

}