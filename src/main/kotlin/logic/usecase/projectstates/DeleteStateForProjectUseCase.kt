package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class DeleteStateForProjectUseCase(
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(stateId: UUID, userId: UUID){
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        if (!adminPermissionCheckerUseCase(userId)) throw AccessDeniedException("Not authorized")

        val state = repository.getStateById(stateId)
        repository.deleteState(stateId)

        val audit = createAudit(state, userId)
        auditRepository.addAuditEntry(audit)

    }

    private fun createAudit(state: TaskStateEntity, userId: UUID): AuditLogEntity {
        val description = "delete  state is deleted successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = description,
            action = Action.Delete,
            userId = userId,
        )
        return audit

    }
}