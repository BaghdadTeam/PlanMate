package org.baghdad.data.repositories.authentication

import data.datasource.local.csv.files.SessionDataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.SessionRepository

class SessionRepositoryImpl(
    private val sessionDataSource: SessionDataSource
): SessionRepository {
    override suspend fun loadSession(): SessionEntity{
            return sessionDataSource.loadSession()
    }

    override suspend fun saveSession(session: SessionEntity): Boolean {
        return sessionDataSource.saveSession(session)
    }

    override suspend fun deleteSession(): Boolean {
        return sessionDataSource.deleteSession()
    }
}