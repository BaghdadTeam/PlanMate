package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.AuditEntity


interface AuditRepository {

    fun addAuditEntry(audit: AuditEntity): Boolean
    fun getAuditByTaskId(taskId: String): List<AuditEntity>?
    fun getAuditByProjectId(projectId: String): List<AuditEntity>?
}