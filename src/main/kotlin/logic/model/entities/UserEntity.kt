package org.baghdad.logic.model.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class UserEntity(
    @BsonId
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val username: String,
    @BsonProperty("hashed_password")
    val hashedPassword: String,
    val type: UserType
)