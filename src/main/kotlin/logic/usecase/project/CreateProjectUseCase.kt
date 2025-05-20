package org.baghdad.logic.usecase.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import java.util.UUID

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(projectName: String, userId : UUID){
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        if (projectName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")

        val project = ProjectEntity(name = projectName, creatorId = userId)
        projectRepository.createProject(project)

        val audit = logProjectCreation(project, userId)
        auditRepository.addAuditEntry(audit)
    }
    private fun logProjectCreation(
        project: ProjectEntity,
        userId: UUID
    ): AuditLogEntity {
        val description = "created project ${project.name}"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = project.id,
            projectId = project.id,
            description = description,
            action = Action.Create,
            userId = userId,
        )
        return audit
    }

}