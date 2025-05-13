package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.*

class GetStateByIdUseCase (
    private val repository: ProjectStatesRepository
) {

    suspend fun invoke(stateId: UUID): TaskStateEntity {
        return repository.getStateById(stateId)
    }
}