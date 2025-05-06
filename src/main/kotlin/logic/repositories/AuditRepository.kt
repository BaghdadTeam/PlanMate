package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.AuditLogEntity
import java.util.UUID


interface AuditRepository {

    fun addAuditEntry(audit: AuditLogEntity)
    fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity>
    fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity>
}