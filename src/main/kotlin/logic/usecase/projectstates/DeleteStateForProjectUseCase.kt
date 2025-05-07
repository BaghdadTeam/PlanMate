package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.getFormattedTimestamp
import java.util.*

class DeleteStateForProjectUseCase (
    private val repository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    fun invoke(stateId: UUID, userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name == UserType.Mate.name) throw Exception("Only Admin can add tasks")
        repository.deleteState(stateId)
        val audit = createAudit(stateId, user)
        auditRepository.addAuditEntry(audit)

    }

    private fun createAudit(stateId: UUID, user: UserEntity):AuditLogEntity {
        val action = "delete  state is deleted successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityId = stateId,
            action = action,
            user = user,
        )
        return audit

    }
}