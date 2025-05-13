package org.baghdad.logic.model.entities

import org.baghdad.logic.model.enums.UserType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class UserEntity(
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val username: String,
    val type: UserType
): Identifiable