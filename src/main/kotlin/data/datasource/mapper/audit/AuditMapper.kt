package org.baghdad.data.datasource.mapper.audit

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.data.datasource.mapper.user.UserMapper
import org.baghdad.data.utils.parseTimestamp
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class AuditMapper : CsvMapper<AuditEntity> {

    override fun header(): String {
        return "id,entityType,entityId,action,user,timestamp"
    }

    override fun deserializer(content: String): AuditEntity {
        val audit = Regex(""",(?=(?:[^\[\]]*\[[^\[\]]*])*[^\[\]]*$)""")
            .split(content)
            .map { it.trim() }
        val userData = UserMapper()
            .deserializer(
                audit[AuditColumns.USER]
                    .removePrefix("[")
                    .removePrefix("]"))

        return AuditEntity(
            id = UUID.fromString(audit[AuditColumns.ID]),
            entityType = Entities.valueOf(audit[AuditColumns.ENTITY_TYPE]),
            entityId = UUID.fromString(audit[AuditColumns.ENTITY_ID]),
            action = audit[AuditColumns.ACTION],
            user = userData,
            timestamp = parseTimestamp(audit[AuditColumns.TIMESTAMP]),
        )


    }


    override fun serializer(item: AuditEntity): String {
        val serializedUser = UserMapper().serializer(item.user)
        return "${item.id},${item.entityType},${item.entityId},${item.action},[${serializedUser}],${item.timestamp}"
    }
}