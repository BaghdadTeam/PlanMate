package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.StateEntity
import java.util.UUID

interface ProjectStatesRepository {

    suspend fun createState(state: StateEntity)
    suspend fun getAllStatesPerProject(projectId: UUID): List<StateEntity>
    suspend fun getStateById(stateId: UUID): StateEntity
    suspend fun editState(stateId: UUID, newState: StateEntity)
    suspend fun deleteState(stateId: UUID)
}