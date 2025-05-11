package org.baghdad.logic.model.entities

import java.time.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.ZoneId
import java.util.UUID

data class AuditLogEntity(

    override val id: UUID = UUID.randomUUID(),
    val entityUnderAudit: String,  // => Project | State | Task
    val entityUnderAuditId : UUID,

    val projectId: UUID,
    val action: Action,
    val description: String,

    val userId: UUID,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))
): Identifiable

enum class Action {
    Delete,Create,Update
}