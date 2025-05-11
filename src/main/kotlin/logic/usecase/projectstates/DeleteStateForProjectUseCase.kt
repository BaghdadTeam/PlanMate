package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.NotAccessException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class DeleteStateForProjectUseCase (
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    suspend fun invoke(stateId: UUID, userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name == UserType.Mate.name) throw NotAccessException("Only Admin can delete states")
        val state = repository.getStateById(stateId)
        repository.deleteState(stateId)
        val audit = createAudit(state, user)
        auditRepository.addAuditEntry(audit)

    }

    private fun createAudit(state: StateEntity, user: UserEntity):AuditLogEntity {
        val action = "delete  state is deleted successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = state.projectId,
            action = action,
            user = user,
        )
        return audit

    }
}