package org.baghdad.logic.usecase.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(projectId: UUID,userId : UUID){
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        val project = projectRepository.getProjectById(projectId)
        projectRepository.deleteProject(projectId)
        val audit = logProjectDeletion(project, user)
        auditRepository.addAuditEntry(audit)
    }


    private fun logProjectDeletion(project: ProjectEntity, user: UserEntity): AuditLogEntity {

        val action = "has been Project ${project.name}"
        return AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            projectId = project.id,
            action = action,
            userId = user.id,
        )
    }
}