package org.baghdad.logic.model.entities

import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

data class StateEntity(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    val name: String,
    val projectId: UUID,
    val creatorId: UUID
): Identifiable
