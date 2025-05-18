package org.baghdad.data.local

import org.baghdad.data.datasource.DataSource
import org.baghdad.data.dto.TaskStateDto
import org.baghdad.data.repositories.toDomain
import org.baghdad.data.repositories.toDto
import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.StateNotFoundException
import java.util.*
import kotlin.collections.find

class ProjectStatesDataSource(
    private val projectStateDataSource: DataSource<TaskStateDto>,
    private val taskDataSource: DataSource<TaskEntity>
) {

    suspend fun getAllStatesForProject(projectId: UUID): List<TaskStateDto> {
        val allData = projectStateDataSource.loadAll()
        val states = allData.filter { it.projectId == projectId }
        if (states.isEmpty()) throw StateNotFoundException("No state found")
        return states
    }

    suspend fun getStateById(id: UUID): TaskStateDto {
        val allData = projectStateDataSource.loadAll()
        return allData.find { it.id == id }
            ?: throw StateNotFoundException("No state found")
    }

    suspend fun createState(state: TaskStateDto) {
        projectStateDataSource.append(state)
    }

    suspend fun editState(state: TaskStateDto) {
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

//
