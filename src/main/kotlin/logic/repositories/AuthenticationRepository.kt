package org.baghdad.logic.repositories

import org.baghdad.logic.entities.UserEntity

interface AuthenticationRepository {

    fun login(username: String, password: String): UserEntity?
}