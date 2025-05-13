package helpers.audit

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

object AuditTestData {
    fun createAuditHelper(
        projectId: UUID = UUID.randomUUID(),
        entityUnderAuditId: UUID = UUID.randomUUID(),
        description: String = "CREATE",
        action: Action = Action.Create,
        userId: UUID = UUID.randomUUID(),
        entityUnderAudit: String = Entities.Task.name,
        timestamp : LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Baghdad"))

    ) = AuditLogEntity(
        projectId = projectId,
        entityUnderAuditId = entityUnderAuditId,
        description = description,
        action = action,
        userId = userId,
        entityUnderAudit = entityUnderAudit,
        timestamp = timestamp
    )


}