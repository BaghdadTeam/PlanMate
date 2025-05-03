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

    fun invoke(stateId: String, userId: UUID){
        if (stateId.isBlank()) throw Exception("Current state not found")
        val user = userRepository.getUserById(userId)
        if (user.type.name == UserType.Mate.name) throw Exception("Only Admin can add tasks")
        repository.deleteState(stateId)
        val audit = createAudit(stateId, user)
        auditRepository.addAuditEntry(audit)

    }

    private fun createAudit(stateId: String, user: UserEntity):AuditEntity {
        val action = "delete  state is deleted successfully"
        val audit = AuditEntity(
            entityType = Entities.Task.name,
            entityId = stateId,
            action = action,
            user = user,
            timestamp = getFormattedTimestamp(),
        )
        return audit

    }
}