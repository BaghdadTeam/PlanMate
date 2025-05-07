package org.baghdad.data.local

import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UserCanNotBeFoundException

class UserDataSource(private val dataSource: DataSource<UserEntity>) {
    fun findUserByUsername(username: String): UserEntity {
        return runBlocking {
            dataSource.loadAll().find { it.username == username }
                ?: throw UserCanNotBeFoundException("User $username not found")
        }
    }
}