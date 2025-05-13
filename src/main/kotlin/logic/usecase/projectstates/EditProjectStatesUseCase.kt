package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class EditProjectStatesUseCase(
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase

) {

    suspend fun invoke(stateId: UUID, newState: StateEntity, userId: UUID) {
        if (!adminPermissionCheckerUseCase(userId)) throw AccessDeniedException("Not authorized")

        repository.editState(stateId, newState)

        val audit = createAudit(newState, userId)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity, userId: UUID): AuditLogEntity {
        val action = "${state.name} state is updated successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = action,
            action = Action.Update,
            userId = userId,
        )
        return audit
    }
}