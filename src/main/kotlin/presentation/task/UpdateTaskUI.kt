package org.baghdad.presentation.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.task.UpdateTaskUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class UpdateTaskUI(
    private val useCase: UpdateTaskUseCase,
    private var tasks: List<TaskEntity>,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        viewer.logMessage("Please enter the task number to update:")
        val taskIndex = reader.readInput()?.toIntOrNull()

        if (taskIndex != null && (taskIndex - 1) in tasks.indices) {
            var newTask = tasks[taskIndex - 1]
            viewer.logMessage("Selected task: ${newTask.title}")

            viewer.logMessage("Please enter the new task title:")
            val taskName = reader.readInput()
            if (taskName != null) {
                newTask = newTask.copy(title = taskName)
            }
            viewer.logMessage("Please enter the new task description:")
            val taskDescription = reader.readInput()
            if (taskDescription != null) {
                newTask = newTask.copy(description = taskDescription)
            }
            try {
                val session = sessionManager.currentSession
                useCase(newTask, session.userId)
            } catch (e: Exception) {
                viewer.logMessage("Failed to update task: ${e.message}")
                return
            }


            viewer.logMessage("Task updated successfully.")

        } else {
            viewer.logMessage("Invalid task number.")
        }
    }
}