package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.CantAddStateWithNoNameException
import org.baghdad.logic.model.exceptions.NotAccessException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class AddTaskStateToProjectUseCase(
    private val projectStatesRepository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(state: TaskStateEntity, userId: UUID) {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("Not authenticated")

        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw NotAccessException("Only Admin can add states")

        if (state.name.isBlank()) throw CantAddStateWithNoNameException("state name can't be empty")

        projectStatesRepository.createState(state)
        val audit = createAudit(state, user)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: TaskStateEntity, user: UserEntity): AuditLogEntity {
        val description = "create ${state.name} state is created successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = description,
            action = Action.Create,
            userId = user.id,
        )
        return audit
    }
}
