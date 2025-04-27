package org.baghdad.logic.repositories

import org.baghdad.logic.entities.AuditEntity


interface AuditRepository {

    fun addAuditEntry(audit: AuditEntity)
    fun getAuditByTaskId(taskId: String): List<AuditEntity>?
    fun getAuditByProjectId(projectId: String): List<AuditEntity>?
}