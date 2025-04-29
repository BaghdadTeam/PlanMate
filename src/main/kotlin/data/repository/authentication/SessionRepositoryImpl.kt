package org.baghdad.data.repository.authentication

import org.baghdad.data.local.SessionDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.SessionRepository

class SessionRepositoryImpl(
    private val sessionDataSource: SessionDataSource
): SessionRepository {
    override fun loadSession(): SessionEntity? {
        TODO("Not yet implemented")
    }

    override fun saveSession(session: SessionEntity): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteSession(): Boolean {
        TODO("Not yet implemented")
    }
}