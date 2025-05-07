package org.baghdad.logic.manager

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.repositories.SessionRepository

class SessionManager(private val sessionRepository: SessionRepository) {

    lateinit var currentSession: SessionEntity

    fun setSession(session: SessionEntity) {
        this.currentSession = session
    }

    fun getActiveSession(): SessionEntity {
        val session = sessionRepository.loadSession()
        if (session.isExpired())
           throw SessionEndedException("Session is expired")
        return session
    }

    fun clearExpiredSession() {
        val session = sessionRepository.loadSession()
        if (session.isExpired())
            sessionRepository.deleteSession()
    }
}
