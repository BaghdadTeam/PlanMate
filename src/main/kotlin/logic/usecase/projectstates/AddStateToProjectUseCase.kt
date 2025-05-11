package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.exceptions.CantAddStateWithNoNameException
import org.baghdad.logic.model.exceptions.NotAccessException
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
        if (user.type != UserType.Admin) throw NotAccessException("Only Admin can add states")

        if (state.name.isBlank()) throw CantAddStateWithNoNameException("state name can't be empty")

        projectStatesRepository.createState(state)
        val audit = createAudit(state, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity,user: UserEntity):AuditLogEntity {
        val action = "create ${state.name} state is created successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            projectId = state.projectId,
            action = action,
            user = user,
        )
        return audit
    }
}