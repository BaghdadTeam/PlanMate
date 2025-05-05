package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository
import java.util.UUID

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository,
) {

    operator fun invoke(projectId: UUID): List<TaskEntity> {
        return taskRepository.getTasksByProjectId(projectId)
    }
}