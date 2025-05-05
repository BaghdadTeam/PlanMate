package org.baghdad.presentation.task

import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTasksByStateIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetTasksByStateIdUI(
    private val getTasksByStateIdUseCase: GetTasksByStateIdUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        val stateId = promptForStateId() ?: return
        tryGetTasks(stateId)
    }

    private fun promptForStateId(): UUID? {
        viewer.logMessage("Please enter the state ID to fetch tasks:")
        val input = reader.readInput()

        if (input.isNullOrBlank()) {
            viewer.logMessage("State ID cannot be empty.")
            return null
        }

        return UUID.fromString(input)
    }

    private fun tryGetTasks(stateId: UUID) {
        try {
            val tasks = getTasksByStateIdUseCase(stateId)
            if (tasks.isEmpty()) {
                viewer.logMessage("No tasks found for the given state.")
            } else {
                viewer.logMessage("Tasks for state $stateId:")
                tasks.forEachIndexed { index, task ->
                    viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
                }
            }
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("No tasks found for state ID: $stateId")
        } catch (e: Exception) {
            viewer.logMessage("Failed to get tasks: ${e.message}")
        }
    }
}