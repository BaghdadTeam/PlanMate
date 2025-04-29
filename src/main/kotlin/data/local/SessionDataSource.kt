package org.baghdad.data.local
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.SessionEntity

class SessionDataSource(
   private val dataSource: DataSource<SessionEntity>
) {
    fun loadSession():SessionEntity? {
        TODO()
    }
    fun saveSession(session: SessionEntity): Boolean{
        TODO()
    }
    fun deleteSession(): Boolean{
        TODO()
    }
}