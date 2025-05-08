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
        val userData = UserMapper()
            .deserializer(
                audit[AuditColumns.USER]
                    .removePrefix("[")
                    .removePrefix("]"))

        return AuditLogEntity(
            id = UUID.fromString(audit[AuditColumns.ID]),
            entityUnderAudit =audit[AuditColumns.ENTITY_TYPE],
            entityId = UUID.fromString(audit[AuditColumns.ENTITY_ID]),
            action = audit[AuditColumns.ACTION],
            user = userData,
            timestamp = parseTimestamp(audit[AuditColumns.TIMESTAMP]),
        )


    }

    override fun getId(item: AuditLogEntity): String {
        return item.id.toString()
    }

    override fun serializer(item: AuditLogEntity): String {
        val serializedUser = UserMapper().serializer(item.user)
        return "${item.id},${item.entityUnderAudit},${item.entityId},${item.action},[${serializedUser}],${item.timestamp}"
    }
}