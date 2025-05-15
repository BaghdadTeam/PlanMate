package org.baghdad.logic.usecase

import org.baghdad.logic.model.entities.TaskStateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID


class ViewServiceUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: ProjectStatesRepository
) {

    suspend fun invoke(projectId: UUID): Map<TaskStateEntity, List<TaskEntity>> {
        try {
            val stateEntities = stateRepository.getAllStatesPerProject(projectId)
            if (stateEntities.isEmpty()) {
                return emptyMap()
            }

            val taskEntities = taskRepository.getTasksByProjectId(projectId)

            val tasksByStateId: Map<String, List<TaskEntity>> = taskEntities.groupBy { it.stateId.toString() }

            return stateEntities.associateWith { state ->
                tasksByStateId[state.id.toString()] ?: emptyList()
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch states or tasks for project $projectId: ${e.message}", e)
        }
    }
}