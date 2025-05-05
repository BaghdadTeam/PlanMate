package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.UUID

class GetAllStatesPerProjectUseCase(
    private val repository: ProjectStatesRepository
) {

    fun invoke(projectId: UUID): List<StateEntity> {
        return repository.getAllStatesPerProject(projectId)
    }
}