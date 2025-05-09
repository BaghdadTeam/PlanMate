package org.baghdad.logic.model.entities

import java.time.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class AuditLogEntity(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    val entityUnderAudit: String,  // => Project | State | Task
    @BsonProperty("project_id")
    val projectId: UUID,
    val action: String,
    val user: UserEntity,
    val timestamp: LocalDateTime = LocalDateTime.now()
): Identifiable