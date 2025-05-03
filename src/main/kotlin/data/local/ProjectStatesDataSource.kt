package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    fun getAllStatesForProject(): List<StateEntity> {
        return projectStateDataSource.loadAll()
    }

    fun getStateById(id: String): StateEntity? {
        val allData = projectStateDataSource.loadAll().toMutableList()
        return allData.find { it.id.toString() == id }
    }

    fun createState(state: StateEntity) {
        projectStateDataSource.append(state)
    }

    fun editState(state: StateEntity) {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val stateIndex = allData.indexOfFirst { it.id == state.id }
        if (stateIndex == -1) throw Exception("No state found")
        allData[stateIndex] = state
        projectStateDataSource.update(allData)
    }

    fun deleteState(stateId: String) {
        val allData = projectStateDataSource.loadAll().toMutableList()
        val state = allData.find { it.id.toString() == stateId } ?: throw Exception("No state found")

        val tasks = taskDataSource.loadAll().toMutableList()
        val filteredTasks = tasks.filterNot { it.stateId == stateId }

        allData.remove(state)
        projectStateDataSource.update(allData)
        taskDataSource.update(filteredTasks)
    }
}