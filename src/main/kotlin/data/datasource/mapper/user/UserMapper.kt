package org.baghdad.data.datasource.mapper.user

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.dto.UserDto
import java.util.*


class UserMapper : CsvMapper<UserDto> {
    override fun header(): String {
        return "id,name,username,hashedPassword,type"
    }

    override fun deserializer(content: String): UserDto {
        val user = content.split(",")
        return UserDto(
            id = UUID.fromString(user[UserColumns.ID]),
            name = user[UserColumns.NAME],
            username = user[UserColumns.USERNAME],
            hashedPassword = user[UserColumns.HASHED_PASSWORD],
            type = user[UserColumns.TYPE]
        )
    }

    override fun getId(item: UserDto): String {
        return item.id.toString()
    }

    override fun serializer(item: UserDto): String {
        return "${item.id},${item.name},${item.username},${item.hashedPassword},${item.type}"
    }
}