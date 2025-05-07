package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.SessionEntity
interface SessionRepository {
    fun loadSession(): SessionEntity
    fun saveSession(session: SessionEntity): Boolean
    fun deleteSession() : Boolean
}