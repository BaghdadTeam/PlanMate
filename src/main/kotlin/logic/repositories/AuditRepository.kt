package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.AuditLogEntity
import java.util.UUID


interface AuditRepository {

    suspend fun addAuditEntry(audit: AuditLogEntity)
    suspend fun getAuditByTaskId(taskId: UUID): List<AuditLogEntity>
    suspend fun getAuditByProjectId(projectId: UUID): List<AuditLogEntity>
}