package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.CantAddStateWithNoNameException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class AddTaskStateToProjectUseCase(
    private val projectStatesRepository: ProjectStatesRepository,
    private val auditRepository: AuditRepository,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase

) {

    suspend fun invoke(state: StateEntity, userId: UUID) {
        if (!adminPermissionCheckerUseCase(userId)) throw AccessDeniedException("Not authorized")
        if (state.name.isBlank()) throw CantAddStateWithNoNameException("state name can't be empty")

        projectStatesRepository.createState(state)

        val audit = createAudit(state, userId)
        auditRepository.addAuditEntry(audit)
    }

    private fun createAudit(state: StateEntity, userId: UUID): AuditLogEntity {
        val description = "create ${state.name} state is created successfully"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Task.name,
            entityUnderAuditId = state.id,
            projectId = state.projectId,
            description = description,
            action = Action.Create,
            userId = userId,
        )
        return audit
    }
}