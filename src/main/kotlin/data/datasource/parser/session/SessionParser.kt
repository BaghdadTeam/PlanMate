package org.baghdad.data.datasource.parser.session

import org.baghdad.data.datasource.CsvParser
import org.baghdad.logic.entities.authentication.SessionEntity

class SessionParser : CsvParser<SessionEntity>{
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