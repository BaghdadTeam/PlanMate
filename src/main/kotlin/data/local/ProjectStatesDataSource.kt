package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.StateNotFoundException
import java.util.*

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    suspend fun getAllStatesForProject(projectId: UUID): List<StateEntity> {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val states = allData.filter { it.projectId == projectId }
        if (states.isEmpty()) throw StateNotFoundException("No state found")
        return states
    }

    suspend fun getStateById(id: UUID): StateEntity {
        val allData = projectStateDataSource.loadAll()
        return allData.find { it.id == id } ?: throw StateNotFoundException("No state found")
    }

    suspend fun createState(state: StateEntity) {
        projectStateDataSource.append(state)
    }

    suspend fun editState(state: StateEntity) {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val state = allData.find { it.id == state.id } ?: throw StateNotFoundException("No state found")
        projectStateDataSource.update(state)
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