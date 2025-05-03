package org.baghdad.data.repositories.projectstates

import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository

class ProjectStatesRepositoryImp(
    private val dataSource: ProjectStatesDataSource
): ProjectStatesRepository {

    override fun getAllStatesPerProject(projectId: String): List<StateEntity> {
        return dataSource.getAllStatesForProject().filter { it.projectId == projectId }
    }

    override fun createState(state: StateEntity) {
        return dataSource.createState(state)
    }

    override fun getStateById(stateId: String): StateEntity? {
        return dataSource.getStateById(stateId)
    }

    override fun editState(stateId: String, newState: StateEntity) {
        dataSource.editState(newState )
    }

    override fun deleteState(stateId: String) {
        return dataSource.deleteState(stateId)
    }


}