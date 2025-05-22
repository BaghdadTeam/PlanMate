package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.StateNotAccessedException
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class EditProjectStatesUseCase(
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager
) {


    suspend operator fun invoke(stateId: UUID, newTaskStateName: String, userId: UUID) {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()

        val state = repository.getStateById(stateId)
        val newState = state.copy(name = newTaskStateName)
        repository.editState(newState)

        val audit = createAudit(newState, userId)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: TaskStateEntity, userId: UUID): AuditLogEntity {
        val description = "${state.name} state is updated successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.State.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = description,
            action = Action.Update,
            userId = userId,
        )
        return audit
    }
}