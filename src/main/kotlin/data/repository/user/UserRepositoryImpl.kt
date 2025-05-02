package org.baghdad.data.repository.user

import org.baghdad.data.local.UserDataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.UserRepository

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

    override fun existsByUsername(username: String): Boolean =
        dataSource.existsByUsername(username)

    override fun getAllUsers(): List<UserEntity> = dataSource.loadUsers()

//    override fun getUserByUsername(username: String): UserEntity? {
//        return dataSource.getUserByUsername(username)
//    }
//    override fun saveUser(user: UserEntity) {
//        dataSource.addUser(user)
//    }
}