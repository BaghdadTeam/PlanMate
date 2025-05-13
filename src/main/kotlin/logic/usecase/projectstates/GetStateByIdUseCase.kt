package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.*

class GetStateByIdUseCase (
    private val repository: ProjectStatesRepository,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(stateId: UUID): TaskStateEntity {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("Unauthorized to get states.")
        return repository.getStateById(stateId)
    }
}