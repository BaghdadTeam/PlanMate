package data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.exceptions.audit.NoProjectFoundException
import org.baghdad.logic.model.exceptions.audit.NoTaskFoundException
import java.util.UUID


class AuditDataSource(
    private val dataSources: DataSource<AuditEntity>
){
     fun createAudit(audit: AuditEntity){
         dataSources.append(audit)
    }

     fun getAuditByTaskId(taskId: UUID): List<AuditEntity> {
        return dataSources.loadAll()
            .filter { (it.entityType == Entities.Task) && (it.entityId == taskId) }
            .takeIf { it.isNotEmpty() }
            ?:throw NoTaskFoundException("No audit found for task with ID: $taskId")

    }

     fun getAuditByProjectId(projectId: UUID): List<AuditEntity> {
         return dataSources.loadAll()
             .filter { (it.entityType == Entities.Project) && (it.entityId == projectId) }
             .takeIf { it.isNotEmpty() }
             ?:throw NoProjectFoundException("No audit found for task with ID: $projectId")
    }
}