package org.baghdad.presentation.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.CsvWriteException
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.model.exceptions.TasksNotFoundException
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
        val taskIndex = promptForTaskIndex() ?: return
        var task = tasks[taskIndex]

        viewer.logMessage("Selected task: ${task.title}")

        task = promptForTitle(task)
        task = promptForDescription(task)

        tryUpdateTask(task)
    }

    private fun promptForTaskIndex(): Int? {
        viewer.logMessage("Please enter the task number to update:")
        val index = reader.readInput()?.toIntOrNull()

        if (index == null || index - 1 !in tasks.indices) {
            viewer.logMessage("Invalid task number.")
            return null
        }

        return index - 1
    }

    private fun promptForTitle(task: TaskEntity): TaskEntity {
        while (true) {
            viewer.logMessage("Please enter the new task title:")
            val title = reader.readInput()
            if (!title.isNullOrBlank()) {
                return task.copy(title = title)
            } else {
                viewer.logMessage("Task title cannot be empty. Please try again.")
            }
        }
    }

    private fun promptForDescription(task: TaskEntity): TaskEntity {
        while (true) {
            viewer.logMessage("Please enter the new task description:")
            val description = reader.readInput()
            if (!description.isNullOrBlank()) {
                return task.copy(description = description)
            } else {
                viewer.logMessage("Task description cannot be empty. Please try again.")
            }
        }
    }

    private fun tryUpdateTask(task: TaskEntity) {
        try {
            val session = sessionManager.currentSession
            useCase(task, session.userId)
            viewer.logMessage("Task updated successfully.")
        } catch (_: TaskWithMissingTitleException) {
            viewer.logMessage("Task title is missing.")
            tryUpdateTask(promptForTitle(task))
        } catch (_: TaskWithMissingDescriptionException) {
            viewer.logMessage("Task description is missing.")
            tryUpdateTask(promptForDescription(task))
        } catch (_: TasksNotFoundException) {
            viewer.logMessage("Task not found.")
        } catch (_: CsvWriteException) {
            viewer.logMessage("Error: Failed to write task to CSV.")
        } catch (e: Exception) {
            viewer.logMessage("Failed to update task: ${e.message}")
        }
    }
}