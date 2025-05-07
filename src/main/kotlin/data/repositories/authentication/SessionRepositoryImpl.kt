package org.baghdad.data.repositories.authentication

import org.baghdad.data.local.SessionDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.SessionRepository

class SessionRepositoryImpl(
    private val sessionDataSource: SessionDataSource
): SessionRepository {
    override fun loadSession(): SessionEntity{
            return sessionDataSource.loadSession()
    }

    override fun saveSession(session: SessionEntity): Boolean {
        return sessionDataSource.saveSession(session)
    }

    override fun deleteSession(): Boolean {
        return sessionDataSource.deleteSession()
    }
}