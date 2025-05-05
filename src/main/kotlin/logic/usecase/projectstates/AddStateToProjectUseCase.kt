package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.utils.getFormattedTimestamp
import java.util.*

class AddStateToProjectUseCase(
    private val projectStatesRepository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    fun invoke(state: StateEntity, userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type.name != UserType.Admin.name) throw Exception("Only Admin can add tasks")
        if (state.name.isBlank()) throw Exception("state name can't be empty")
        projectStatesRepository.createState(state)
        val audit = createAudit(state, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity,user: UserEntity):AuditEntity {
        val action = "create ${state.name} state is created successfully"
        val audit = AuditEntity(
            entityType = Entities.Task.name,
            entityId = state.id,
            action = action,
            user = user,
            timestamp = getFormattedTimestamp(),
        )
        return audit
    }
}