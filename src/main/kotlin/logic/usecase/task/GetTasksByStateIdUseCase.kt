package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository

class GetTasksByStateIdUseCase(
    private val taskRepository: TaskRepository,
) {
    operator fun invoke(stateId: String): List<TaskEntity> {
        return taskRepository.getTasksByStateId(stateId)
    }
}