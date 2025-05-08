package org.baghdad.data.repositories.audit

import data.local.AuditDataSource
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource

) : AuditRepository {
    override suspend fun addAuditEntry(audit: AuditLogEntity) {
        auditDataSource.createAudit(audit)
    }

    override suspend fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByTaskId(taskId)
    }

    override suspend fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByProjectId(projectId)
    }
}

