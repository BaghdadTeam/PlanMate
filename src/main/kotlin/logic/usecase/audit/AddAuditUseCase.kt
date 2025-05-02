package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.model.exceptions.audit.EmptyActionInAuditEntityException
import org.baghdad.logic.repositories.AuditRepository

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {
    operator fun invoke(auditEntity: AuditEntity) {
        validateAuditEntity(auditEntity)
        auditRepository.addAuditEntry(auditEntity)
    }

    private fun validateAuditEntity(auditEntity: AuditEntity) {
        if (auditEntity.action.isBlank()) throw EmptyActionInAuditEntityException("Action cannot be empty")
    }
}