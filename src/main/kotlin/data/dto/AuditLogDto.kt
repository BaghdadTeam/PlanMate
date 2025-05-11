package org.baghdad.data.dto

import org.baghdad.logic.model.entities.Identifiable
import org.baghdad.logic.model.entities.UserEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

data class AuditLogDto(
    override val id: UUID = UUID.randomUUID(),
    val entityUnderAudit: String,
    val projectId: UUID,
    val action: String,
    val user: UserEntity,
    val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))
) : Identifiable