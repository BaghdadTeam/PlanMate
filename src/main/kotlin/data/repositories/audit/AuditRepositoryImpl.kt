package org.baghdad.data.repositories.audit

import data.datasource.local.csv.files.AuditDataSource
import org.baghdad.data.repositories.toDomain
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.*

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource

) : AuditRepository {
    override suspend fun addAuditEntry(audit: AuditLogEntity) {
        auditDataSource.createAudit(audit.toDto())
    }

    override suspend fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByTaskId(taskId).map { it.toDomain() }
    }

    override suspend fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByProjectId(projectId).map { it.toDomain() }
    }
}

