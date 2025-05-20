package org.baghdad.data.dto.project

import org.baghdad.logic.model.entities.Identifiable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class ProjectDto(
    @BsonId
    override val id: UUID,
    @BsonProperty("name")
    val name: String,
    @BsonProperty("creator_id")
    val creatorId: UUID
) : Identifiable

