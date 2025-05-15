package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(projectId: UUID,userId : UUID){
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()

        val project = projectRepository.getProjectById(projectId)
        projectRepository.deleteProject(projectId)

        val audit = logProjectDeletion(project, userId)
        auditRepository.addAuditEntry(audit)
    }


    private fun logProjectDeletion(project: ProjectEntity, userId: UUID): AuditLogEntity {

        val description = "has been Project ${project.name}"
        return AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = project.id,
            projectId = project.id,
            description = description,
            action = Action.Delete,
            userId = userId,
        )
    }
}