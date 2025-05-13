package org.baghdad.logic.usecase.project

import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.enums.Entities
import org.baghdad.logic.model.exceptions.AccessDeniedException
import org.baghdad.logic.repositories.AuditRepository
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.repositories.UserRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository,
    private val auditRepository: AuditRepository
) {
    suspend operator fun invoke(projectId: UUID,userId : UUID){
        val user = userRepository.getUserById(userId)
        if (user.type != UserType.Admin) throw AccessDeniedException("Not authorized")
        val project = projectRepository.getProjectById(projectId)
        projectRepository.deleteProject(projectId)
        val audit = logProjectDeletion(project, user)
        auditRepository.addAuditEntry(audit)
    }


    private fun logProjectDeletion(project: ProjectEntity, user: UserEntity): AuditLogEntity {

        val description = "has been Project ${project.name}"
        return AuditLogEntity(
            entityUnderAudit = Entities.Project.name,
            entityUnderAuditId = project.id,
            projectId = project.id,
            description = description,
            action = Action.Delete,
            userId = user.id,
        )
    }
}