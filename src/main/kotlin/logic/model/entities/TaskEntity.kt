package org.baghdad.logic.model.entities

import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

data class TaskEntity(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String,
    val stateId: UUID,
    val projectId: UUID,
    val creatorId: UUID
): Identifiable