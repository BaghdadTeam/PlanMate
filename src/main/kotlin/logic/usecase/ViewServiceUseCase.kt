package org.baghdad.logic.usecase
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.StateRepository
import org.baghdad.logic.repositories.TaskRepository


class ViewServiceUseCase(
    private val taskRepository: TaskRepository,
    private val stateRepository: StateRepository
) {

    fun swimlane(projectId: String): Result<Map<StateEntity, List<TaskEntity>>> {
        return try {
            val stateEntities = stateRepository.getAllStatesPerProject(projectId)
            val states = stateEntities.map { stateEntity ->
                StateEntity(
                    id = stateEntity.id,
                    name = stateEntity.name ,
                    projectId = stateEntity.projectId,
                    creatorId = stateEntity.creatorId
                )
            }

            val taskEntities = taskRepository.getTasksByProjectId(projectId)
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

            val tasksByStateId:Map<String,List<TaskEntity>> = tasks.groupBy { it.stateId }
            val groupedTasks = states.associateWith { state ->
                tasksByStateId[state.id.toString()] ?: emptyList<TaskEntity>()
            }

            Result.success(groupedTasks)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch states or tasks for project $projectId: ${e.message}", e))
        }
    }

    }
