package org.baghdad.data.mapper

import org.baghdad.data.dto.AuditLogDto
import org.baghdad.logic.model.entities.AuditLogEntity

fun AuditLogDto.toDomain(): AuditLogEntity { // read
    return AuditLogEntity(
        id = id,
        entityUnderAudit = entityUnderAudit,
        projectId = projectId,
        action = action,
        user = user,
        timestamp = timestamp
    )
}
fun AuditLogEntity.toDto(): AuditLogDto { // write
    return AuditLogDto(
        id = id,
        entityUnderAudit = entityUnderAudit,
        projectId = projectId,
        action = action,
        user = user,
        timestamp = timestamp
    )
}