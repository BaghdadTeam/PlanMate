package org.baghdad.presentation.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.DeleteTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class DeleteTaskUI(
    private val useCase: DeleteTaskUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute(tasks: List<TaskEntity>) {
        val taskIndex = promptForTaskIndex(tasks) ?: return
        val task = tasks[taskIndex]

        viewer.logMessage("Selected task: ${task.title}")
        tryDeleteTask(task)
    }

    private fun promptForTaskIndex(tasks: List<TaskEntity>): Int? {
        viewer.logMessage("Please enter the task number to delete:")
        val index = reader.readInput()?.toIntOrNull()

        if (index == null || index - 1 !in tasks.indices) {
            viewer.logMessage("Invalid task number.")
            return null
        }

        return index - 1
    }

    private fun tryDeleteTask(task: TaskEntity) {
        try {
            val session = sessionManager.currentSession
            useCase(task.id, session.userId)

            viewer.logMessage("Task deleted successfully.")
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("Task not found.")
        } catch (e: Exception) {
            viewer.logMessage("Failed to delete task: ${e.message}")
        }
    }
}