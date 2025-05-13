package org.baghdad.data.dto

import org.baghdad.logic.model.entities.Identifiable
import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

data class TaskStateDto(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val projectId: UUID,
    val creatorId: UUID
) : Identifiable