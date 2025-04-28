package org.baghdad.logic.repositories.authentication

import org.baghdad.logic.entities.authentication.SessionEntity

interface SessionRepository {
    fun loadSession(): SessionEntity?
    fun saveSession(session: SessionEntity): Boolean
    fun deleteSession() : Boolean
}