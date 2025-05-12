package org.baghdad.data.dto

import org.baghdad.logic.model.entities.Identifiable
import org.baghdad.logic.model.entities.UserEntity
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

data class AuditLogDto(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    val entityUnderAudit: String,  // => Project | State | Task
    @BsonProperty("project_id")
    val projectId: UUID,
    val action: String,
    val user: UserEntity,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))
): Identifiable