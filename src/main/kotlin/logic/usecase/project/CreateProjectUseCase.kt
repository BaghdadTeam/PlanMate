package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.AuditLogEntity
import org.baghdad.logic.model.entities.Entities
import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.model.exceptions.EmptyProjectNameException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.UUID

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    suspend operator fun invoke(projectName: String, userId : UUID){
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        if (projectName.isBlank()) throw EmptyProjectNameException("Project name can't be empty")

        val project = ProjectEntity(name = projectName, creatorId = user.id)
        projectRepository.createProject(project)

        val audit = logProjectCreation(project, user)
        auditRepository.addAuditEntry(audit)
    }
    private fun logProjectCreation(
        project: ProjectEntity,
        user: UserEntity
    ): AuditLogEntity {
        val action = "created project ${project.name}"
        val audit = AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            projectId = project.id,
            action = action,
            user = user,
        )
        return audit
    }

}