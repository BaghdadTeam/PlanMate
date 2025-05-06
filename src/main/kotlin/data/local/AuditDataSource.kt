package data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import java.util.UUID


class AuditDataSource(
    private val dataSources: DataSource<AuditLogEntity>
) {
    fun createAudit(audit: AuditLogEntity) {
        dataSources.append(audit)
    }

    fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity> {
        return dataSources.loadAll()
            .filter { (it.entityUnderAudit == Entities.Task.name) && (it.entityId == taskId) }
            .takeIf { it.isNotEmpty() }
            ?: throw NoTaskFoundException("No audit found for task with ID: $taskId")

    }

    fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity> {
        return dataSources.loadAll()
            .filter { (it.entityUnderAudit == Entities.Project.name) && (it.entityId == projectId) }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectFoundException("No audit found for task with ID: $projectId")
    }
}