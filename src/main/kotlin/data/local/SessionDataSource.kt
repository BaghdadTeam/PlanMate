package org.baghdad.data.local
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionNotFoundException

class SessionDataSource(
    private val dataSource: DataSource<SessionEntity>
) {
    fun loadSession(): SessionEntity {
        return dataSource.loadAll().lastOrNull()
            ?: throw SessionNotFoundException("No session found")
    }

    fun saveSession(session: SessionEntity): Boolean {
        dataSource.append(session)
        return dataSource.loadAll().any { it.id == session.id }
    }

    fun deleteSession(): Boolean {
        dataSource.update(emptyList())
        return dataSource.loadAll().isEmpty()
    }
}