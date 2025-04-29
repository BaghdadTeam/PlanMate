package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.UserEntity

interface UserRepository {

    fun createUser(username: String, userCreator: UserEntity)
    fun getUserByUsername(username: String): UserEntity?
    fun getAllUsers(): List<UserEntity>
}