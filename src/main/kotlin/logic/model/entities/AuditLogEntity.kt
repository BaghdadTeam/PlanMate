package org.baghdad.logic.model.entities

import java.time.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.ZoneId
import java.util.UUID

data class AuditLogEntity(
    @BsonId
    override val id: UUID = UUID.randomUUID(),
    @BsonProperty("entity_under_audit")
    val entityUnderAudit: String,  // => Project | State | Task
    @BsonProperty("entity_under_audit_id")
    val entityUnderAuditId : UUID,
    @BsonProperty("project_id")
    val projectId: UUID,
    val action: Action,
    val description: String,
    @BsonProperty("user_id")
    val userId: UUID,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))
): Identifiable

enum class Action {
    Delete,Create,Update
}