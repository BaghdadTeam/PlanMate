package data.datasource.local.csv.files

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.AuditLogDto
import org.baghdad.logic.model.exceptions.NoProjectFoundException
import org.baghdad.logic.model.exceptions.NoTaskFoundException
import java.util.*
import kotlin.collections.filter


class AuditDataSource(
    private val dataSources: DataSource<AuditLogDto>
) {
    suspend fun createAudit(audit: AuditLogDto) {
        dataSources.append(audit)
    }

    suspend fun getAuditByTaskId(taskId: UUID): List<AuditLogDto> {
        return dataSources.loadAll()
            .filter {(it.entityUnderAuditId == taskId.toString()) }
            .takeIf { it.isNotEmpty() }
            ?: throw NoTaskFoundException("No audit found for task with ID: $taskId")
    }

    suspend fun getAuditByProjectId(projectId: UUID): List<AuditLogDto> {
        return dataSources.loadAll()
            .filter { it.projectId == projectId.toString() }
            .takeIf { it.isNotEmpty() }
            ?: throw NoProjectFoundException("No audit found for project with ID: $projectId")
    }
}