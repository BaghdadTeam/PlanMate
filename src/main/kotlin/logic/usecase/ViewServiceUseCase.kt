package org.baghdad.logic.usecase

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID


class ViewServiceUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: ProjectStatesRepository,
    private val sessionManager: SessionManager
) {

    suspend fun swimlane(projectId: UUID): Map<StateEntity, List<TaskEntity>> {
        if (!sessionManager.isAuthenticated()) throw UnauthorizedException("User Not logged in.")
        try {
            // Fetch states for the project
            val stateEntities = stateRepository.getAllStatesPerProject(projectId)
            if (stateEntities.isEmpty()) {
                // If no states are found, return an empty map
                return emptyMap()
            }

            // Fetch tasks for the project
            val taskEntities = taskRepository.getTasksByProjectId(projectId)

            // Group tasks by their stateId, return empty lists for states without tasks
            val tasksByStateId: Map<String, List<TaskEntity>> = taskEntities.groupBy { it.stateId.toString() }

            // Map states to corresponding tasks, use empty list if no tasks for the state
            return stateEntities.associateWith { state ->
                tasksByStateId[state.id.toString()] ?: emptyList()
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch states or tasks for project $projectId: ${e.message}", e)
        }
    }
}