package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.UserEntity

class UserDataSource(private val csvDataSource: DataSource<UserEntity>) {
    fun findUserByUsername(username: String): UserEntity? {
        TODO()
    }
}