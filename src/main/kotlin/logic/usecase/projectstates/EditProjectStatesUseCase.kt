package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
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

    suspend fun invoke(stateId: UUID, newState: StateEntity , userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name != UserType.Admin.name) throw NotAccessException("Only Admin can edit states")
        repository.editState(stateId, newState)
        val audit = createAudit(newState, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity, user: UserEntity):AuditLogEntity {
        val action = "create ${state.name} state is updated successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = state.projectId,
            action = action,
            user = user,
        )
        return audit
    }
}