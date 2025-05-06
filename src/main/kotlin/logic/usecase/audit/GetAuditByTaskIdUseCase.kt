package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import java.util.UUID

class GetAuditByTaskIdUseCase(
    val auditRepository: AuditRepository
) {
    operator fun invoke(taskId : UUID): List<AuditLogEntity> {
        return auditRepository.getAuditByTaskId(taskId)
    }
}