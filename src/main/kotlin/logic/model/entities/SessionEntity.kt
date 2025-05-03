package org.baghdad.logic.model.entities
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

data class SessionEntity(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val token: String,
    val loginTime: LocalDateTime,

) {
    fun isExpired(): Boolean {

        val expireAfter = Duration.ofMinutes(EXPIRE_AFTER_IN_MINUTES.toLong())
        return Duration.between(loginTime, LocalDateTime.now()) > expireAfter
    }

    companion object
    {
        private const val EXPIRE_AFTER_IN_MINUTES = 30
    }
}
