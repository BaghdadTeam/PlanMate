package org.baghdad.data.datasource.mapper.sesssion

import org.baghdad.data.datasource.CsvMapper
import org.baghdad.logic.model.entities.SessionEntity

class SessionMapper : CsvMapper<SessionEntity>{
    override fun header(): String {
        TODO("Not yet implemented")
    }

    override fun serializer(item: SessionEntity): String {
        TODO("Not yet implemented")
    }

    override fun deserializer(content: String): SessionEntity {
        TODO("Not yet implemented")
    }
}