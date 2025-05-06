package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.exceptions.EmptyActionInAuditEntityException
import org.baghdad.logic.repositories.AuditRepository

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {
    operator fun invoke(auditLogEntity: AuditLogEntity) {
        validateAuditEntity(auditLogEntity)
        auditRepository.addAuditEntry(auditLogEntity)
    }

    private fun validateAuditEntity(auditLogEntity: AuditLogEntity) {
        if (auditLogEntity.action.isBlank()) throw EmptyActionInAuditEntityException("Action cannot be empty")
    }
}