package org.baghdad.logic.usecase.audit

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class GetAuditByProjectIdUseCase(
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(projectId: UUID): Pair<List<AuditLogEntity>, List<UserEntity>> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        val projectAudit = auditRepository.getAuditByProjectId(projectId)
        val sortedAudit = projectAudit.sortedByDescending { it.timestamp }
        val users = sortedAudit.map { auditEntry ->
            userRepository.getUserById(auditEntry.userId)
        }

        return Pair(sortedAudit, users)

    }
}