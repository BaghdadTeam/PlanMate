package org.baghdad.presentation.task

import org.baghdad.logic.model.exceptions.TasksNotFoundException
import org.baghdad.logic.usecase.task.GetTasksByProjectIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class GetTasksByProjectIdUI(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        val projectId = promptForProjectId() ?: return
        tryGetTasks(projectId)
    }

    private fun promptForProjectId(): String? {
        viewer.logMessage("Please enter the project ID to fetch tasks:")
        val input = reader.readInput()

        if (input.isNullOrBlank()) {
            viewer.logMessage("Project ID cannot be empty.")
            return null
        }

        return input
    }

    private fun tryGetTasks(projectId: String) {
        try {
            val tasks = getTasksByProjectIdUseCase(projectId)
            if (tasks.isEmpty()) {
                viewer.logMessage("No tasks found for the given project.")
            } else {
                viewer.logMessage("Tasks for project $projectId:")
                tasks.forEachIndexed { index, task ->
                    viewer.logMessage("${index + 1}. ${task.title} - ${task.description}")
                }
            }
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("No tasks found for project ID: $projectId")
        } catch (e: Exception) {
            viewer.logMessage("Failed to get tasks: ${e.message}")
        }
    }
}