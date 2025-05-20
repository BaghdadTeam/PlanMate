package org.baghdad.data.repositories

import org.baghdad.data.dto.AuditLogDto
import org.baghdad.data.utils.parseTimestamp
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import java.util.UUID

fun AuditLogDto.toDomain(): AuditLogEntity { // read
    return AuditLogEntity(
        id = id,
        entityUnderAudit = entityUnderAudit,
        entityUnderAuditId = UUID.fromString(entityUnderAuditId),
        projectId = UUID.fromString(projectId),
        action = Action.valueOf(action),
        description = description,
        userId = UUID.fromString(userId),
        timestamp = parseTimestamp(timestamp)
    )
}
fun AuditLogEntity.toDto(): AuditLogDto { // write
    return AuditLogDto(
        id = id,
        entityUnderAudit = entityUnderAudit,
        entityUnderAuditId = entityUnderAuditId.toString(),
        projectId = projectId.toString(),
        action = action.name,
        description = description,
        userId = userId.toString(),
        timestamp = timestamp.toString()
    )
}