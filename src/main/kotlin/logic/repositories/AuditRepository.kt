package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.AuditEntity
import java.util.UUID


interface AuditRepository {

    fun addAuditEntry(audit: AuditEntity)
    fun getAuditByTaskId(taskId: UUID): List<AuditEntity>
    fun getAuditByProjectId(projectId: UUID): List<AuditEntity>
}