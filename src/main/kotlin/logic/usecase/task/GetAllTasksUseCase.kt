package org.baghdad.logic.usecase.task

import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.repositories.TaskRepository

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(): List<TaskEntity> {
        return taskRepository.getAllTasks()
    }
}