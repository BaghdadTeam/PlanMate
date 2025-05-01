package helpers.authentication

import org.baghdad.logic.model.entities.SessionEntity
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random

object SessionTestData {
    object TestSession {
        val id = UUID.randomUUID()
        val token = UUID.randomUUID().toString()
        val loginTime = LocalDateTime.now()
        val userId = UUID.randomUUID().toString()
        val line = "$id,$userId,$token,$loginTime"
    }
    val baseSession = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID().toString(),
        token = UUID.randomUUID().toString(),
        loginTime = LocalDateTime.now(),
    )
    val baseSessionWithExpiredDate = SessionEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID().toString(),
        token = UUID.randomUUID().toString(),
        loginTime = LocalDateTime.parse("2025-04-30T10:55:40.986676") ,
    )
}
