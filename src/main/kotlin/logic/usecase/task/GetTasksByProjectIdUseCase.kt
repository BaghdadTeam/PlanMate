package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository,
) {

    operator fun invoke(projectId: String): List<TaskEntity> {
        return taskRepository.getTasksByProjectId(projectId)
    }
}