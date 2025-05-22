package org.baghdad.logic.usecase.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.Action
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(projectName: String, userId : UUID){
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()
        if (projectName.isBlank()) throw EmptyProjectNameException()

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