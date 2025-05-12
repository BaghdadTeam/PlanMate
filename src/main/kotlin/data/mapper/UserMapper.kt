package org.baghdad.data.mapper

import org.baghdad.data.dto.user.UserDto
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType


fun UserDto.toDomain(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        username = this.username,
        type = UserType.valueOf(this.type)
    )
}
fun UserEntity.toDto(hashedPassword: String): UserDto {
    return UserDto(
        id = this.id,
        username = this.username,
        name = this.name,
        hashedPassword = hashedPassword,
        type = this.type.toString()
    )
}