package org.baghdad.data.local

import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionNotFoundException

class SessionDataSource(
    private val dataSource: DataSource<SessionEntity>
) {
    fun loadSession(): SessionEntity {
        return runBlocking {
            dataSource.loadAll().lastOrNull()
                ?: throw SessionNotFoundException("No session found")
        }
    }

    fun saveSession(session: SessionEntity): Boolean {
        return runBlocking {
            dataSource.append(session)
            dataSource.loadAll().any { it.id == session.id }
        }
    }

    fun deleteSession(): Boolean {
        return runBlocking {
            val session = loadSession()
            dataSource.delete(session)
            dataSource.loadAll().none { it.id == session.id }
        }
    }
}