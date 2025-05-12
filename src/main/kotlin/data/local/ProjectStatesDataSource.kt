package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.TaskStateDto
import org.baghdad.data.mapper.toDomain
import org.baghdad.data.mapper.toDto
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.StateNotFoundException
import java.util.*
import kotlin.collections.find

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<TaskStateDto>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    suspend fun getAllStatesForProject(projectId: UUID): List<StateEntity> {
        val allData = projectStateDataSource.loadAll()
        val states = allData.filter { it.projectId == projectId }.map { it.toDomain() }
        if (states.isEmpty()) throw StateNotFoundException("No state found")
        return states
    }

    suspend fun getStateById(id: UUID): StateEntity {
        val allData = projectStateDataSource.loadAll()
        return allData.find { it.id == id }?.toDomain()
            ?: throw StateNotFoundException("No state found")
    }

    suspend fun createState(state: StateEntity) {
        projectStateDataSource.append(state.toDto())
    }

    suspend fun editState(state: StateEntity) {
        val allData = projectStateDataSource.loadAll()
        allData.find { it.id == state.id }
            ?: throw StateNotFoundException("No state found")

        projectStateDataSource.update(state.toDto())
    }

    suspend fun deleteState(stateId: UUID) {
        val state = projectStateDataSource.loadAll()
            .firstOrNull { it.id == stateId }
            ?: throw StateNotFoundException("No state found")

        projectStateDataSource.delete(state)

        taskDataSource.loadAll()
            .filter { it.stateId == stateId }
            .forEach { taskDataSource.delete(it) }
    }
}