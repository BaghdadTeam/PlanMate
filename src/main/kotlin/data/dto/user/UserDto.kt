package org.baghdad.data.dto.user

import org.baghdad.logic.model.entities.Identifiable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class UserDto(
    @BsonId
    override val id: UUID,
    val name: String,
    val username: String,
    @BsonProperty("hashed_password")
    val hashedPassword: String,
    val type: String,
): Identifiable
