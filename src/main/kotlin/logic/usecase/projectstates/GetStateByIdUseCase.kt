package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.UUID

class GetStateByIdUseCase (
    private val repository: ProjectStatesRepository
) {

    suspend fun invoke(stateId: UUID): StateEntity {
        return repository.getStateById(stateId) ?: throw Exception("No state found")
    }
}