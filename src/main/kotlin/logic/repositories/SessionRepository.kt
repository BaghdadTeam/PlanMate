package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.SessionEntity
interface SessionRepository {
    suspend fun loadSession(): SessionEntity
    suspend fun saveSession(session: SessionEntity): Boolean
    suspend fun deleteSession() : Boolean
}