package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class AddStateToProjectUseCase(
    private val projectStatesRepository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) {

    suspend fun invoke(state: StateEntity, userId: UUID){
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw Exception("Only Admin can add tasks")

        if (state.name.isBlank()) throw Exception("state name can't be empty")

        projectStatesRepository.createState(state)
        val audit = createAudit(state, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity,user: UserEntity):AuditLogEntity {
        val action = "create ${state.name} state is created successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityId = state.id,
            action = action,
            user = user,
        )
        return audit
    }
}