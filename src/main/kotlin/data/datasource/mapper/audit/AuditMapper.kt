package org.baghdad.data.datasource.mapper.audit

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.dto.AuditLogDto
import java.util.UUID

class AuditMapper : CsvMapper<AuditLogDto> {

    override fun header(): String {
        return "id,entityUnderAudit,entityUnderAuditId,projectId,description,action,userId,timestamp"
    }

    override fun deserializer(content: String): AuditLogDto {
        val audit = Regex(""",(?=(?:[^\[\]]*\[[^\[\]]*])*[^\[\]]*$)""")
            .split(content)
            .map { it.trim() }


        return AuditLogDto(
            id = UUID.fromString(audit[AuditColumns.ID]),
            entityUnderAudit = audit[AuditColumns.ENTITY_UNDER_AUDIT_TYPE],
            entityUnderAuditId = audit[AuditColumns.ENTITY_UNDER_AUDIT_TYPE_ID],
            projectId = audit[AuditColumns.PROJECT_ID],
            description = audit[AuditColumns.DESCRIPTION],
            action = audit[AuditColumns.ACTION],
            userId = audit[AuditColumns.USER_ID],
            timestamp = audit[AuditColumns.TIMESTAMP],
        )


    }

    override fun getId(item: AuditLogDto): String {
        return item.id.toString()
    }

    override fun serializer(item: AuditLogDto): String {
        return "${item.id}," +
                "${item.entityUnderAudit}," +
                "${item.entityUnderAuditId}," +
                "${item.projectId}," +
                "${item.description}," +
                "${item.action}," +
                "${item.userId}," +
                item.timestamp
    }
}