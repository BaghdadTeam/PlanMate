package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.StateNotAccessedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class EditProjectStatesUseCase (
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(stateId: UUID, newTaskStateName: String, userId: UUID){

        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()

        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw StateNotAccessedException("Only Admin can edit states")
        val state = repository.getStateById(stateId)

        val newState = state.copy(name = newTaskStateName)
        repository.editState(newState)
        val audit = createAudit(newState, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: TaskStateEntity, user: UserEntity):AuditLogEntity {
        val action = "${state.name} state has been updated successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.State.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = action,
            action = Action.Update,
            userId = user.id,
        )
        return audit
    }
}