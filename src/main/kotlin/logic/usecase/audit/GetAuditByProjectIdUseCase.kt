package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class GetAuditByProjectIdUseCase(
    val auditRepository: AuditRepository
){
    operator fun invoke(projectId: UUID): List<AuditEntity> {
        return auditRepository.getAuditByProjectId(projectId)
    }

}