package org.baghdad.data.repositories.audit

import data.local.AuditDataSource
import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class AuditRepositoryImpl(
    private val auditDataSource: AuditDataSource

) : AuditRepository {
    override fun addAuditEntry(audit: AuditEntity) {
        auditDataSource.createAudit(audit)
    }

    override fun getAuditByTaskId(taskId: UUID): List<AuditEntity> {
        return auditDataSource.getAuditByTaskId(taskId)
    }

    override fun getAuditByProjectId(projectId: UUID): List<AuditEntity> {
        return auditDataSource.getAuditByProjectId(projectId)
    }
}

