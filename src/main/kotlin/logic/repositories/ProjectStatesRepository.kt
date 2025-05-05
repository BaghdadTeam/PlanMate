package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.StateEntity
import java.util.UUID

interface ProjectStatesRepository {

    fun createState(state: StateEntity)
    fun getAllStatesPerProject(projectId: UUID): List<StateEntity>
    fun getStateById(stateId: UUID): StateEntity?
    fun editState(stateId: UUID, newState: StateEntity)
    fun deleteState(stateId: UUID)
}