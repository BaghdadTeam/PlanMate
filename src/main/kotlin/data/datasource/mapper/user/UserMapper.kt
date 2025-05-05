package org.baghdad.data.datasource.mapper.user

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import java.util.*

class UserMapper : CsvMapper<UserEntity> {
    override fun header(): String {
        return "id,name,username,hashedPassword,type"
    }

    override fun serializer(item: UserEntity): String {
        return "${item.id},${item.name},${item.username},${item.hashedPassword},${item.type.name}"
    }


    override fun deserializer(content: String): UserEntity {
        val user = content.split(",")
        return UserEntity(
            id = UUID.fromString(user[UserColumns.ID]),
            name = user[UserColumns.NAME],
            username = user[UserColumns.USERNAME],
            hashedPassword = user[UserColumns.HASHED_PASSWORD],
            type = UserType.valueOf(user[UserColumns.TYPE])
        )
    }



}