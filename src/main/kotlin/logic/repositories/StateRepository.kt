package org.baghdad.logic.repositories

import org.baghdad.logic.entities.StateEntity

interface StateRepository {

    fun createState(state: StateEntity)
    fun getAllStatesPerProject(projectId: String): List<StateEntity>
    fun getStateById(id: String): StateEntity?
    fun editState(stateId: String, newState: StateEntity): Boolean
    fun deleteState(stateId: String): Boolean
}