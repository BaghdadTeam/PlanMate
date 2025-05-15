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

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(projectName: String, userId : UUID){

        if (!sessionManager.isAuthenticated()) throw UnauthorizedException()

        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException()
        if (projectName.isBlank()) throw EmptyProjectNameException()

        val project = ProjectEntity(name = projectName, creatorId = user.id)
        projectRepository.createProject(project)

        val audit = logProjectCreation(project, user)
        auditRepository.addAuditEntry(audit)
    }
    private fun logProjectCreation(
        project: ProjectEntity,
        user: UserEntity
    ): AuditLogEntity {
        val description = "created project ${project.name}"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = project.id,
            projectId = project.id,
            description = description,
            action = Action.Create,
            userId = user.id,
        )
        return audit
    }

}