package org.baghdad.logic.storage.audit

import org.baghdad.logic.entities.AuditEntity
import org.baghdad.logic.storage.base.BasicStorage

interface AuditStorage
    : BasicStorage<AuditEntity> {
    fun getByTaskId(taskId: String): List<AuditEntity>
    fun getByProjectId(projectId: String): List<AuditEntity>
}