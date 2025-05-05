package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository,
) {

    operator fun invoke(id: UUID): TaskEntity {
        return taskRepository.getTaskById(id)
    }
}