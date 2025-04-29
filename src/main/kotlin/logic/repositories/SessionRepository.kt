package org.baghdad.logic.repositories

import SessionEntity

interface SessionRepository {
    fun loadSession(): SessionEntity?
    fun saveSession(session: SessionEntity): Boolean
    fun deleteSession() : Boolean
}