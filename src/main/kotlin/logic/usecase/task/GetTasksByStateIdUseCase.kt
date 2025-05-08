package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetTasksByStateIdUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(stateId: UUID): List<TaskEntity> {
        return taskRepository.getTasksByStateId(stateId)
    }
}