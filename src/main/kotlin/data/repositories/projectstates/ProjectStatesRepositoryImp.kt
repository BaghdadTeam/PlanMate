package org.baghdad.data.repositories.projectstates

import org.baghdad.data.local.ProjectStatesDataSource
import org.baghdad.data.repositories.toDomain
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import java.util.UUID

class ProjectStatesRepositoryImp(
    private val dataSource: ProjectStatesDataSource
) : ProjectStatesRepository {

    override suspend fun getAllStatesPerProject(projectId: UUID): List<TaskStateEntity> {
        return dataSource.getAllStatesForProject(projectId).map { it.toDomain() }
    }

    override suspend fun createState(state: TaskStateEntity) {
        return dataSource.createState(state.toDto())
    }

    override suspend fun getStateById(stateId: UUID): TaskStateEntity {
        return dataSource.getStateById(stateId).toDomain()
    }

    override suspend fun editState(newState: TaskStateEntity) {
        dataSource.editState(newState.toDto())
    }

    override suspend fun deleteState(stateId: UUID) {
        return dataSource.deleteState(stateId)
    }

}
