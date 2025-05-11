package org.baghdad.data.datasource.mapper.audit

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.data.utils.parseTimestamp
import org.baghdad.logic.model.entities.AuditLogEntity
import java.util.*

class AuditMapper : CsvMapper<AuditLogEntity> {

    override fun header(): String {
        return "id,entityType,entityId,action,user,timestamp"
    }

    override fun deserializer(content: String): AuditLogEntity {
        val audit = Regex(""",(?=(?:[^\[\]]*\[[^\[\]]*])*[^\[\]]*$)""")
            .split(content)
            .map { it.trim() }


        return AuditLogEntity(
            id = UUID.fromString(audit[AuditColumns.ID]),
            entityUnderAudit =audit[AuditColumns.ENTITY_TYPE],
            projectId = UUID.fromString(audit[AuditColumns.ENTITY_ID]),
            action = audit[AuditColumns.ACTION],
            userId = UUID.fromString(audit[AuditColumns.USER_ID]),
            timestamp = parseTimestamp(audit[AuditColumns.TIMESTAMP]),
        )


    }

    override fun getId(item: AuditLogEntity): String {
        return item.id.toString()
    }

    override fun serializer(item: AuditLogEntity): String {
        return "${item.id},${item.entityUnderAudit},${item.projectId},${item.action},${item.userId},${item.timestamp}"
    }
}