package org.baghdad.data.datasource.parser.audit

import org.baghdad.data.datasource.CsvParser
import org.baghdad.data.datasource.parser.user.UserParser
import org.baghdad.logic.model.entities.AuditEntity
import java.util.*

class AuditParser : CsvParser<AuditEntity> {
    override fun header(): String {
        return "id,entityType,entityId,action,user,timestamp"
    }

    override fun deserializer(content: String): AuditEntity {
        val audit = Regex(""",(?=(?:[^\[\]]*\[[^\[\]]*])*[^\[\]]*$)""")
            .split(content)
            .map { it.trim() }
        val userData = UserParser()
            .deserializer(
                audit[AuditColumns.USER]
                    .removePrefix("[")
                    .removePrefix("]")
            )

        return AuditEntity(
            id = UUID.fromString(audit[AuditColumns.ID]),
            entityType = audit[AuditColumns.ENTITY_TYPE],
            entityId = audit[AuditColumns.ENTITY_ID],
            action = audit[AuditColumns.ACTION],
            user = userData,
            timestamp = audit[AuditColumns.TIMESTAMP],
        )
    }

    override fun serializer(item: AuditEntity): String {
        val serializedUser = UserParser().serializer(item.user)
        return "${item.id},${item.entityType},${item.entityId},${item.action},[${serializedUser}],${item.timestamp}"
    }
}