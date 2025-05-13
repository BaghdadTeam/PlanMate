package org.baghdad.logic.usecase.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager

) {
    suspend operator fun invoke(projectId: UUID, projectNewName: String, userId: UUID) {
        if(!sessionManager.isAuthenticated()) throw UnauthorizedException("Not Authenticated user")
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        if (projectNewName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")

        val existing = projectRepository.getProjectById(projectId)
        val updated = existing.copy(name = projectNewName)
        projectRepository.editProject(updated)

        val audit = logProjectUpdate(existing, updated, user)
        auditRepository.addAuditEntry(audit)

    }

    private fun logProjectUpdate(
        oldProject: ProjectEntity,
        newProject: ProjectEntity,
        user: UserEntity
    ): AuditLogEntity {

        val description = "name change form “${oldProject.name}” to “${newProject.name}” "
        return AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = oldProject.id,
            projectId = oldProject.id,
            description = description,
            action = Action.Update,
            userId = user.id,
        )
    }
}