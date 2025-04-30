package org.baghdad.data.datasource.mapper.sesssion

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.SessionEntity
import java.time.LocalDateTime
import java.util.UUID

class SessionMapper : CsvMapper<SessionEntity>{
    override fun header(): String {
      return SessionHeader.HEADER
    }

    override fun serializer(item: SessionEntity): String {
       return "${item.id},${item.userId},${item.token},${item.loginTime}"
    }

    override fun deserializer(content: String): SessionEntity {
       val session = content.split(",")
        return SessionEntity(
            id = UUID.fromString(session[SessionColumns.ID]),
            userId = session[SessionColumns.USER_ID],
            token = session[SessionColumns.TOKEN],
            loginTime = LocalDateTime.parse(session[SessionColumns.LOGIN_TIME])
        )
    }
}