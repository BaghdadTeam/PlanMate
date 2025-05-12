package org.baghdad.logic.usecase.audit

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetAuditByProjectIdUseCase(
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(projectId: UUID): List<AuditLogEntity> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        val projectAudit = auditRepository.getAuditByProjectId(projectId)
        return projectAudit.sortedByDescending { it.timestamp }
    }
}