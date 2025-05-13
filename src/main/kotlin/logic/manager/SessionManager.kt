package org.baghdad.logic.manager

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.repositories.SessionRepository

class SessionManager(private val sessionRepository: SessionRepository) {

    lateinit var currentSession: SessionEntity
    fun setSession(session: SessionEntity) {
        this.currentSession = session
    }
    suspend fun getActiveSession(): SessionEntity {
        val session = sessionRepository.loadSession()
        if (session.isExpired()) {
            clearExpiredSession()
            throw SessionEndedException("Session expired")
        }
        currentSession = session
        return currentSession
    }

    suspend fun isAuthenticated(): Boolean {
        return try {
            getActiveSession()
            true
        } catch (_: SessionEndedException) {
            false
        }
    }

    private suspend fun clearExpiredSession() {
        sessionRepository.deleteSession()
    }
}
