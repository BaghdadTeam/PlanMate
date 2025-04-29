package helpers.authentication

import java.time.LocalDateTime
import java.util.UUID

object SessionTestData {
    object TestSession {
        val id = UUID.randomUUID()
        val token = UUID.randomUUID().toString()
        val loginTime = LocalDateTime.now()
        val userId = UUID.randomUUID().toString()
        val line = "$id,$userId,$token,$loginTime"
    }
}