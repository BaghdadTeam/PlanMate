package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository

class GetAllStatesPerProjectUseCase(
    private val repository: ProjectStatesRepository
) {

    fun invoke(projectId: String): List<StateEntity>{
        return repository.getAllStatesPerProject(projectId)
    }

}