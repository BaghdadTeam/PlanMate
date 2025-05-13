package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetAuditByTaskIdUseCase(
    val auditRepository: AuditRepository,
    val userRepository: UserRepository
) {
    suspend operator fun invoke(taskId : UUID): Pair<List<AuditLogEntity>, List<UserEntity>> {
        val taskAudit = auditRepository.getAuditByTaskId(taskId)
        val sortedAudit = taskAudit.sortedByDescending { it.timestamp }
        val users = sortedAudit.map { auditEntry ->
            userRepository.getUserById(auditEntry.userId)
        }
        return Pair(sortedAudit, users)
    }
}