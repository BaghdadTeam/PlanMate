package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.getFormattedTimestamp
import java.util.*

class EditProjectStatesUseCase (
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    fun invoke(stateId: String, newState: StateEntity , userId: UUID){
        if (stateId.isBlank()) throw Exception("state id can't be empty")
        val user = userRepository.getUserById(userId)
      //  if (user.type.name != UserType.Admin.name) throw Exception("Only Admin can add tasks")
        repository.editState(stateId, newState)
        val audit = createAudit(newState, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity, user: UserEntity):AuditEntity {
        val action = "create ${state.name} state is updated successfully"
        val audit = AuditEntity(
            entityType = Entities.Task.name,
            entityId = state.id.toString(),
            action = action,
            user = user,
            timestamp = getFormattedTimestamp(),
        )
        return audit

    }

}