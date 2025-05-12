package org.baghdad.logic.usecase.audit

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class GetAuditByTaskIdUseCase(
    val auditRepository: AuditRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(taskId : UUID): List<AuditLogEntity> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        return auditRepository.getAuditByTaskId(taskId)
    }
}