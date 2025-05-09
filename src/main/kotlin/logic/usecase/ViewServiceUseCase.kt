package org.baghdad.logic.usecase

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.ProjectStatesRepository
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID


class ViewServiceUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: ProjectStatesRepository
) {

    suspend fun swimlane(projectId: UUID): Map<StateEntity, List<TaskEntity>> {
         try {
            val stateEntities = stateRepository.getAllStatesPerProject(projectId)
             if(stateEntities.isEmpty()){
                 return emptyMap()
             }

            val taskEntities = taskRepository.getTasksByProjectId(projectId)
             if(taskEntities.isEmpty()){
                 throw Exception("No tasks found for project $projectId")
             }
            val tasks = taskEntities.map { taskEntity ->
                TaskEntity(
                    id = taskEntity.id,
                    title = taskEntity.title,
                    description = taskEntity.description,
                    stateId = taskEntity.stateId,
                    projectId = taskEntity.projectId,
                    creatorId = taskEntity.creatorId

                )
            }
             val states = stateEntities.map { stateEntity ->
                 StateEntity(
                     id = stateEntity.id,
                     name = stateEntity.name,
                     projectId = stateEntity.projectId,
                     creatorId = stateEntity.creatorId
                 )
             }

            val tasksByStateId: Map<String, List<TaskEntity>> = tasks.groupBy { it.stateId.toString() }
            val groupedTasks = states.associateWith { state ->
                tasksByStateId[state.id.toString()] ?: emptyList<TaskEntity>()
            }

            return groupedTasks
        } catch (e: Exception) {
                throw Exception(
                    "Failed to fetch states or tasks for project $projectId: ${e.message}",
                    e

                )
        }
    }
}
