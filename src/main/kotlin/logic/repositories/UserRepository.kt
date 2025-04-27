package org.baghdad.logic.repositories

import org.baghdad.logic.entities.UserEntity

interface UserRepository {

    fun createUser(user: UserEntity)
    fun getUserByUsername(username: String): UserEntity?
    fun getAllUsers(): List<UserEntity>
}