package org.baghdad.data.repositories.projectstates

import data.datasource.local.csv.files.ProjectStatesDataSource
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.*

class ProjectStatesRepositoryImp(
    private val dataSource: ProjectStatesDataSource
) : ProjectStatesRepository {

    override suspend fun getAllStatesPerProject(projectId: UUID): List<TaskStateEntity> {
        return dataSource.getAllStatesForProject(projectId)
    }

    override suspend fun createState(state: TaskStateEntity) {
        return dataSource.createState(state)
    }

    override suspend fun getStateById(stateId: UUID): TaskStateEntity {
        return dataSource.getStateById(stateId)
    }

    override suspend fun editState(newState: TaskStateEntity) {
        dataSource.editState(newState)
    }

    override suspend fun deleteState(stateId: UUID) {
        return dataSource.deleteState(stateId)
    }

}
