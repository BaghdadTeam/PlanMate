package org.baghdad.presentation.task

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.StateTransitionUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.*

class TaskManagementGatherUI(
    private val viewer: Viewer,
    private val reader: Reader,
    private val createTaskUI: CreateTaskUI,
    private val editTaskUI: UpdateTaskUI,
    private val deleteTaskUI: DeleteTaskUI,
    private val changeTaskStateUI: StateTransitionUI,
) {
    fun execute(projectData :Map<StateEntity, List<TaskEntity>>,projectId: UUID) {
        showOptions(projectData,projectId)

    }
    private fun showOptions(project: Map<StateEntity, List<TaskEntity>>,projectId: UUID) {

        viewer.logMessage("=== Task Management ===")
        viewer.logMessage("1. Create Task")
        viewer.logMessage("2. Edit Task")
        viewer.logMessage("3. Delete Task")
        viewer.logMessage("4. Change Task State")
        viewer.logMessage("0. Back to Previous Screen")
        viewer.logMessage("Enter your choice: ")

        when (reader.readInput()?.trim()) {
            "1" -> createTaskUI.execute(projectId)
            "2" -> promptEdit(project.values.flatten())
            "3" -> promptDelete(project.values.flatten())
            "4" -> changeTaskStateUI.execute(project.keys.toList(),project.values.flatten())
            "0" -> return
            else -> {
                viewer.logError("Invalid choice. Please try again.")
//                showOptions(projectId, tasksStatesIds, tasks)
            }
        }
    }

    private fun promptEdit(tasks: List<TaskEntity>) {
        editTaskUI.execute(tasks)
    }

    private fun promptDelete(tasks: List<TaskEntity>) {
        deleteTaskUI.execute(tasks)
    }
}