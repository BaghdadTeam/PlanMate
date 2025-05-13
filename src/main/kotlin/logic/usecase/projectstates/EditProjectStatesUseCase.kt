package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.enums.UserType
import org.baghdad.logic.model.exceptions.NotAccessException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class EditProjectStatesUseCase (
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    suspend fun invoke(stateId: UUID, newState: TaskStateEntity, userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name != UserType.Admin.name) throw NotAccessException("Only Admin can edit states")
        repository.editState(stateId, newState)
        val audit = createAudit(newState, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: TaskStateEntity, user: UserEntity):AuditLogEntity {
        val action = "create ${state.name} state is updated successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = action,
            action = Action.Update,
            userId = user.id,
        )
        return audit
    }
}