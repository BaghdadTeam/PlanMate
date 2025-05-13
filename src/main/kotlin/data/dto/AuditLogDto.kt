package org.baghdad.data.dto

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.Identifiable
import org.baghdad.logic.model.entities.UserEntity
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

data class AuditLogDto(
    @BsonId
    override val id: UUID,
    @BsonProperty("entity_Under_Audit")
    val entityUnderAudit: String,  // => Project | State | Task
    @BsonProperty("entity_Under_Audit_id")
    val entityUnderAuditId : String,
    @BsonProperty("project_id")
    val projectId: String,
    val action: String,
    val description: String,
    @BsonProperty("user_id")
    val userId: String,
    val timestamp: String
): Identifiable