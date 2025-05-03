package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.StateEntity

interface ProjectStatesRepository {

    fun createState(state: StateEntity)
    fun getAllStatesPerProject(projectId: String): List<StateEntity>
    fun getStateById(stateId: String): StateEntity?
    fun editState(stateId: String, newState: StateEntity)
    fun deleteState(stateId: String)
}