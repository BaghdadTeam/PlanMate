package helpers.audit

import org.baghdad.data.dto.AuditLogDto
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.enums.Entities
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

object AuditTestData {
    fun createAuditHelper(
        id : UUID = UUID.randomUUID(),
        projectId: UUID = UUID.randomUUID(),
        entityUnderAuditId: UUID = UUID.randomUUID(),
        description: String = "CREATE",
        action: Action = Action.Create,
        userId: UUID = UUID.randomUUID(),
        entityUnderAudit: String = Entities.Task.name,
        timestamp : LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))

    ) = AuditLogEntity(
        id = id,
        projectId = projectId,
        entityUnderAuditId = entityUnderAuditId,
        description = description,
        action = action,
        userId = userId,
        entityUnderAudit = entityUnderAudit,
        timestamp = timestamp
    )
    fun createAuditDtoHelper(
        id:UUID = UUID.randomUUID(),
        projectId: UUID = UUID.randomUUID(),
        entityUnderAuditId: UUID = UUID.randomUUID(),
        description: String = "CREATE",
        action: Action = Action.Create,
        userId: UUID = UUID.randomUUID(),
        entityUnderAudit: String = Entities.Task.name,
        timestamp : LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))

    ) = AuditLogDto(
        id = id,
        projectId = projectId.toString()    ,
        entityUnderAuditId = entityUnderAuditId.toString(),
        description = description,
        action = action.name,
        userId = userId.toString(),
        entityUnderAudit = entityUnderAudit,
        timestamp = timestamp.toString()
    )

}