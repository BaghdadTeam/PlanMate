package org.baghdad.logic.usecase.audit

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.exceptions.EmptyActionInAuditEntityException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository

class AddAuditUseCase(
    private val auditRepository: AuditRepository,
  private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(auditLogEntity: AuditLogEntity) {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        validateAuditEntity(auditLogEntity)
        auditRepository.addAuditEntry(auditLogEntity)
    }

    private fun validateAuditEntity(auditLogEntity: AuditLogEntity) {
        if (auditLogEntity.action.isBlank()) throw EmptyActionInAuditEntityException("Action cannot be empty")
    }
}