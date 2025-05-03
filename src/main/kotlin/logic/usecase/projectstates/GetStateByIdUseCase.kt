package org.baghdad.logic.usecase.projectstate

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository

class GetStateByIdUseCase (
    private val repository: ProjectStatesRepository
) {

    fun invoke(stateId: String): StateEntity? {
        return repository.getStateById(stateId) ?: throw Exception("No state found")
    }
}