package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity

class ProjectStatesDataSource(
    private val dataSource: DataSource<StateEntity>
) {

    fun getAllStatesForProject(): List<StateEntity> {
        return dataSource.loadAll()
    }

    fun getStateById(id: String): StateEntity? {
        val allData = dataSource.loadAll().toMutableList()
        return allData.find { it.id.toString() == id }
    }

    fun createState(state: StateEntity) {
        dataSource.append(state)
    }

    fun editState(state: StateEntity) {
        val allData = dataSource.loadAll().toMutableList()
        val stateIndex = allData.indexOfFirst { it.id == state.id }
        if (stateIndex == -1) throw Exception("No state found")
        allData[stateIndex] = state
        dataSource.update(allData)
    }

    fun deleteState(stateId: String) {
        val allData = dataSource.loadAll().toMutableList()
        val state = allData.find { it.id.toString() == stateId } ?: throw Exception("No state found")
        allData.remove(state)
        dataSource.update(allData)
    }


}