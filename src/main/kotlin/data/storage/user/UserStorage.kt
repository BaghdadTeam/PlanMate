package org.baghdad.data.storage.user

import org.baghdad.logic.entities.UserEntity
import org.baghdad.data.storage.base.BasicStorage
import org.baghdad.data.storage.base.DeletableStorage

interface UserStorage
    : BasicStorage<UserEntity>,
    DeletableStorage<UserEntity> {

    fun findUserByUsername(username: String): UserEntity?
}