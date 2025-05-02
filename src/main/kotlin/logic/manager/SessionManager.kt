package org.baghdad.logic.manager

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.logic.repositories.SessionRepository

class SessionManager(private val sessionRepository: SessionRepository) {

    lateinit var currentSession: SessionEntity

    fun setSession(session: SessionEntity) {
        this.currentSession = session
    }

    fun getActiveSession(): Result<SessionEntity> {
        val session = sessionRepository.loadSession()
            ?: return Result.failure(SessionNotFoundException("Session not found"))
        if (session.isExpired())
            return Result.failure(SessionEndedException("Session is expired"))
        return Result.success(session)
    }

    fun clearExpiredSession() {
        val session = sessionRepository.loadSession()
        if (session != null && session.isExpired())
            sessionRepository.deleteSession()
    }
}
