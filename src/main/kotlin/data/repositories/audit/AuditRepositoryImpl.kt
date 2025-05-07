package org.baghdad.data.repositories.audit

import data.local.AuditDataSource
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource

) : AuditRepository {
    override fun addAuditEntry(audit: AuditLogEntity) {
        auditDataSource.createAudit(audit)
    }

    override fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByTaskId(taskId)
    }

    override fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity> {
        return auditDataSource.getAuditByProjectId(projectId)
    }
}

