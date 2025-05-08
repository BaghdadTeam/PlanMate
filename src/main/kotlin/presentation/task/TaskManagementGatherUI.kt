package org.baghdad.presentation.task

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.StateTransitionUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.ui.SwimlaneUI
import java.util.UUID

class TaskManagementGatherUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val swimlaneUI: SwimlaneUI,
    private val createTaskUI: CreateTaskUI,
    private val editTaskUI: UpdateTaskUI,
    private val deleteTaskUI: DeleteTaskUI,
    private val viewServiceUseCase: ViewServiceUseCase,
    private val changeTaskStateUI: StateTransitionUI,
) {

    fun execute(projectId: UUID) {
        // 1) Render the current board
        viewer.logMessage(swimlaneUI.renderAsciiSwimlane(projectId))
        // 2) Show menu
        showOptions(projectId)
    }

    private fun showOptions(projectId: UUID) {
        viewer.logMessage("")
        viewer.logMessage("1. Create Task")
        viewer.logMessage("2. Edit Task")
        viewer.logMessage("3. Delete Task")
        viewer.logMessage("4. Change Task State")
        viewer.logMessage("Enter your choice: ")

        when (reader.readInput()?.trim()) {
            "1" -> createInFirstState(projectId)
            "2" -> promptEdit(projectId)
            "3" -> promptDelete(projectId)
            "4" -> changeTaskStateUI.execute(projectId)
            else -> {
                viewer.logError("Invalid choice. Please try again.")
                showOptions(projectId)
            }
        }
    }

    private fun createInFirstState(projectId: UUID) {
        val result = runBlocking { viewServiceUseCase.swimlane(projectId) }
        if (result.isFailure) {
            viewer.logError("Cannot fetch swimlane: ${result.exceptionOrNull()?.message}")
            return
        }

        val stateMap = result.getOrNull().orEmpty()
        val firstState: StateEntity? = stateMap.keys.firstOrNull()

        if (firstState == null) {
            viewer.logError("No states available in this project.")
            return
        }

        createTaskUI.execute(projectId, firstState.id)
    }

    private fun promptEdit(projectId: UUID) {
        val tasks = changeTaskStateUI.listTasks(projectId)
        if (tasks.isEmpty()) {
            viewer.logError("No tasks to edit.")
        } else {
            editTaskUI.execute(tasks)
        }
    }

    private fun promptDelete(projectId: UUID) {
        val tasks = changeTaskStateUI.listTasks(projectId)
        if (tasks.isEmpty()) {
            viewer.logError("No tasks to delete.")
        } else {
            deleteTaskUI.execute(tasks)
        }
    }
}