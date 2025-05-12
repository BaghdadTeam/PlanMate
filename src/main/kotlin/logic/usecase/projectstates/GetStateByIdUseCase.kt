package org.baghdad.logic.usecase.projectstates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.*

class GetStateByIdUseCase (
    private val repository: ProjectStatesRepository,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(stateId: UUID): StateEntity {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        return repository.getStateById(stateId)
    }
}