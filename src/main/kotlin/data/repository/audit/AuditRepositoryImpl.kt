package org.baghdad.data.repository.audit

import org.baghdad.logic.entities.AuditEntity
import org.baghdad.logic.repositories.AuditRepository

class AuditRepositoryImpl: AuditRepository {
    override fun addAuditEntry(audit: AuditEntity): Boolean {
        return true
    }

    override fun getAuditByTaskId(taskId: String): List<AuditEntity>? {
        TODO("Not yet implemented")
    }

    override fun getAuditByProjectId(projectId: String): List<AuditEntity>? {
        TODO("Not yet implemented")
    }
}