package org.baghdad.logic.usecase.audit

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetAuditByProjectIdUseCase(
    private val auditRepository: AuditRepository,
    private val projectStatesRepository: ProjectStatesRepository,
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(projectId: UUID): List<AuditLogEntity> {
        val projectAudit = auditRepository.getAuditByProjectId(projectId)

        return projectAudit.sortedByDescending { it.timestamp }
    }
}