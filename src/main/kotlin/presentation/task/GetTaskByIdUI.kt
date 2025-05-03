package org.baghdad.presentation.task

import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTaskByIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetTaskByIdUI(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val reader: Reader,
    private val viewer: Viewer
) {

    fun execute() {
        val id = promptForTaskId() ?: return
        tryGetTask(id)
    }

    private fun promptForTaskId(): UUID? {
        viewer.logMessage("Please enter the task ID:")
        val input = reader.readInput()

        return try {
            UUID.fromString(input)
        } catch (e: Exception) {
            viewer.logMessage("Invalid task ID format.")
            null
        }
    }

    private fun tryGetTask(id: UUID) {
        try {
            val task = getTaskByIdUseCase(id)
            viewer.logMessage("Task found:")
            viewer.logMessage("Title: ${task.title}")
            viewer.logMessage("Description: ${task.description}")
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("No task found with ID: $id")
        } catch (e: Exception) {
            viewer.logMessage("Failed to get task: ${e.message}")
        }
    }
}