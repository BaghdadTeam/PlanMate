package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.UserEntity

interface UserRepository {

    fun createUser(user: UserEntity)
    fun getAllUsers(): List<UserEntity>
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): UserEntity?

}
