package org.baghdad.logic.storage.user

import org.baghdad.logic.entities.UserEntity
import org.baghdad.logic.storage.base.BasicStorage
import org.baghdad.logic.storage.base.DeletableStorage

interface UserStorage
    : BasicStorage<UserEntity>,
    DeletableStorage<UserEntity> {

    fun findUserByUsername(username: String): UserEntity?
}