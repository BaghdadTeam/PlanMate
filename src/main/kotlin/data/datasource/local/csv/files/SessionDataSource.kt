package data.datasource.local.csv.files

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionNotFoundException

class SessionDataSource(
    private val dataSource: DataSource<SessionEntity>
) {
    suspend fun loadSession(): SessionEntity {
        return dataSource.loadAll().lastOrNull()
            ?: throw SessionNotFoundException("No session found")
    }

    suspend fun saveSession(session: SessionEntity): Boolean {
        dataSource.append(session)
        return dataSource.loadAll().any { it.id == session.id }
    }

    suspend fun deleteSession(): Boolean {
        val session = loadSession()
        dataSource.delete(session)
        return dataSource.loadAll().none { it.id == session.id }
    }
}