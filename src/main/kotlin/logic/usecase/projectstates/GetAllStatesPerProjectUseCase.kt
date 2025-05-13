package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.UUID

class GetAllStatesPerProjectUseCase(
    private val repository: ProjectStatesRepository
) {

    suspend fun invoke(projectId: UUID): List<TaskStateEntity> {
        return repository.getAllStatesPerProject(projectId)
    }
}