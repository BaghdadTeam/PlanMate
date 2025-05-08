package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import java.util.*

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    suspend fun getAllStatesForProject(): List<StateEntity> {
        return projectStateDataSource.loadAll()
    }

    suspend fun getStateById(id: UUID): StateEntity? {
        val allData = getAllStatesForProject()
        return allData.find { it.id == id }
    }

    suspend fun createState(state: StateEntity) {
        projectStateDataSource.append(state)
    }

    suspend fun editState(state: StateEntity) {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val state = allData.first { it.id == state.id }
        projectStateDataSource.update(state)
    }

    suspend fun deleteState(stateId: UUID) {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val state = allData.find { it.id == stateId } ?: throw Exception("No state found")

        val tasks = taskDataSource.loadAll().toMutableList()
        val filteredTasks = tasks.filterNot { it.stateId == stateId }

        projectStateDataSource.delete(state)
        filteredTasks.forEach {
            taskDataSource.delete(it)
        }
    }
}