package org.baghdad.presentation.task

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.model.exceptions.TaskWithMissingDescriptionException
import org.baghdad.logic.model.exceptions.TaskWithMissingTitleException
import org.baghdad.logic.usecase.task.CreateTaskUseCase
import org.baghdad.presentation.app.Feature
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.*

class CreateTaskUI(
    private val createTaskUseCase: CreateTaskUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) : Feature {
    override val id: Int = 1
    override val name: String = "Create Task"
    override fun execute() {
        TODO("Not yet implemented")
    }

    fun execute(projectId: UUID, stateId: UUID) {

        val user = sessionManager.currentSession

        val task = promptForTaskDetails(user.id, projectId, stateId) ?: return

        tryCreateTask(task, user.id)
    }

    private fun promptForTaskDetails(id: UUID, projectId: UUID, stateId: UUID): TaskEntity? {

        val title = promptForTitle() ?: ""
        val description = promptForDescription() ?: ""

        return TaskEntity(
            title = title,
            description = description,
            stateId = stateId,
            projectId = projectId,
            creatorId = id
        )
    }

    private fun promptForTitle(): String? {
        while (true) {
            viewer.logMessage("Please enter the task title:")
            val title = reader.readInput()
            if (!title.isNullOrBlank()) {
                return title
            } else {
                viewer.logError("Task title cannot be empty. Please try again.")
            }
        }
    }

    private fun promptForDescription(): String? {
        while (true) {
            viewer.logMessage("Please enter the task description:")
            val description = reader.readInput()
            if (!description.isNullOrBlank()) {
                return description
            } else {
                viewer.logError("Task description cannot be empty. Please try again.")
            }
        }
    }

    private fun tryCreateTask(task: TaskEntity, userId: UUID) {
        try {
            createTaskUseCase(task, userId)
            viewer.logMessage("Task created successfully.")
        } catch (e: TaskWithMissingTitleException) {
            viewer.logError("Task title is missing.")
            val updatedTask = promptForTitle()?.let { task.copy(title = it) } ?: task
            tryCreateTask(updatedTask, userId)
        } catch (e: TaskWithMissingDescriptionException) {
            viewer.logError("Task description is missing.")
            val updatedTask = promptForDescription()?.let { task.copy(description = it) } ?: task
            tryCreateTask(updatedTask, userId)
        } catch (e: Exception) {
            viewer.logError("Failed to create task: ${e.message}")
        }
    }
}