package org.baghdad.data.repositories.audit

import org.baghdad.data.local.AuditDataSource
import org.baghdad.data.mapper.toDomain
import org.baghdad.data.mapper.toDto
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

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

