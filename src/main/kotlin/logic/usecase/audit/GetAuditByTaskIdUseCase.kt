package org.baghdad.logic.usecase.audit

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetAuditByTaskIdUseCase(
    val auditRepository: AuditRepository,
    val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(taskId : UUID): Pair<List<AuditLogEntity>, List<UserEntity>> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        val taskAudit = auditRepository.getAuditByTaskId(taskId)
        val sortedAudit = taskAudit.sortedByDescending { it.timestamp }
        val users = sortedAudit.map { auditEntry ->
            userRepository.getUserById(auditEntry.userId)
        }
        return Pair(sortedAudit, users)
    }
}