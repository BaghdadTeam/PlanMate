package org.baghdad.data.repositories.projectstates

import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.UUID

class ProjectStatesRepositoryImp(
    private val dataSource: ProjectStatesDataSource
): ProjectStatesRepository {

    override fun getAllStatesPerProject(projectId: UUID): List<StateEntity> {
        return dataSource.getAllStatesForProject().filter { it.projectId == projectId }
    }

    override fun createState(state: StateEntity) {
        return dataSource.createState(state)
    }

    override fun getStateById(stateId: UUID): StateEntity? {
        return dataSource.getStateById(stateId)
    }

    override fun editState(stateId: UUID, newState: StateEntity) {
        dataSource.editState(newState )
    }

    override fun deleteState(stateId: UUID) {
        return dataSource.deleteState(stateId)
    }


}