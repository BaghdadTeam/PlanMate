package helpers.authentication

import org.baghdad.logic.model.entities.SessionEntity
import java.time.LocalDateTime
import java.util.*

object SessionTestData {
    object TestSession {
        val id: UUID = UUID.randomUUID()
        val token = UUID.randomUUID().toString()
        val loginTime: LocalDateTime = LocalDateTime.now()
        val userId: UUID = UUID.randomUUID()
        val line = "$id,$userId,$token,$loginTime"
    }

    val baseSession = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        token = UUID.randomUUID().toString(),
        loginTime = LocalDateTime.now(),
    )
    val baseSessionWithExpiredDate = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        token = UUID.randomUUID().toString(),
        loginTime = LocalDateTime.parse("2025-04-30T10:55:40.986676"),
    )
}