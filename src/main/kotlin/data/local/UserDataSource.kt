package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UserCanNotBeFoundException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import java.util.UUID

class UserDataSource(
    private val dataSource: DataSource<UserEntity>
) {
    fun loadUsers(): List<UserEntity> = dataSource.loadAll()


    fun addUser(user: UserEntity) {
        dataSource.append(user)

class UserDataSource(private val dataSource: DataSource<UserEntity>) {
    fun findUserByUsername(username: String): UserEntity {
       return  dataSource.loadAll().find { it.username == username }
           ?:throw UserCanNotBeFoundException("User $username not found")
    }

    fun existsByUsername(username: String): Boolean =
        loadUsers().any { it.username == username }

    fun findUserByUsername(username: String): UserEntity =
        loadUsers().firstOrNull  { it.username == username }
            ?: throw UserNotFoundException("User $username not found")
}

//    fun getUserById(userId: UUID): UserEntity {
//        return loadUsers().firstOrNull { it.id == userId }
//            ?: throw UserNotFoundException("User with ID $userId not found")
//    }

