package org.baghdad.logic.repositories

import org.baghdad.logic.model.entities.TaskStateEntity
import java.util.UUID

interface ProjectStatesRepository {

    suspend fun createState(state: TaskStateEntity)
    suspend fun getAllStatesPerProject(projectId: UUID): List<TaskStateEntity>
    suspend fun getStateById(stateId: UUID): TaskStateEntity
    suspend fun editState(stateId: UUID, newState: TaskStateEntity)
    suspend fun deleteState(stateId: UUID)
}