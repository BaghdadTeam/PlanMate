package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase

) {
    suspend operator fun invoke(projectId: UUID, projectNewName: String, userId: UUID) {
        if (!adminPermissionCheckerUseCase(userId)) throw AccessDeniedException("Not authorized")
        if (projectNewName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")

        val existing = projectRepository.getProjectById(projectId)
        val updated = existing.copy(name = projectNewName)
        projectRepository.editProject(updated)

        val audit = logProjectUpdate(existing, updated, userId)
        auditRepository.addAuditEntry(audit)

    }

    private fun logProjectUpdate(
        oldProject: ProjectEntity,
        newProject: ProjectEntity,
        userID: UUID
    ): AuditLogEntity {

        val description = "name change form “${oldProject.name}” to “${newProject.name}” "
        return AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = oldProject.id,
            projectId = oldProject.id,
            description = description,
            action = Action.Update,
            userId = userID,
        )
    }
}