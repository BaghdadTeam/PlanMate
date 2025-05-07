package org.baghdad.data.local

import kotlinx.coroutines.runBlocking
import org.baghdad.data.datasource.DataSource
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import java.util.*

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<StateEntity>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    fun getAllStatesForProject(): List<StateEntity> {
        return runBlocking {
            projectStateDataSource.loadAll()
        }
    }

    fun getStateById(id: UUID): StateEntity? {
        val allData = getAllStatesForProject()
        return allData.find { it.id == id }
    }

    fun createState(state: StateEntity) {
        runBlocking {
            projectStateDataSource.append(state)
        }
    }

    fun editState(state: StateEntity) {
        runBlocking {
            val allData = projectStateDataSource.loadAll().toMutableList()
            val state = allData.first { it.id == state.id }
            projectStateDataSource.update(state)
        }
    }

    fun deleteState(stateId: UUID) {
        runBlocking {
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
}