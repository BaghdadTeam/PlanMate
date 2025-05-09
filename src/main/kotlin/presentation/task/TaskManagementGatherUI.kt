package org.baghdad.presentation.task

import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.model.entities.TaskEntity
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

    fun execute(projectId: UUID, tasksStatesIds: List<StateEntity>, tasks: List<TaskEntity>) {
        showOptions(projectId, tasksStatesIds, tasks)
    }

    private fun showOptions(projectId: UUID, tasksStatesIds: List<StateEntity>, tasks: List<TaskEntity>) {
        viewer.logMessage("")
        viewer.logMessage("1. Create Task")
        viewer.logMessage("2. Edit Task")
        viewer.logMessage("3. Delete Task")
        viewer.logMessage("4. Change Task State")
        viewer.logMessage("Enter your choice: ")

        when (reader.readInput()?.trim()) {
            "1" -> createTaskUI.execute(projectId, tasksStatesIds.first().id)
            "2" -> promptEdit(tasks)
            "3" -> promptDelete(tasks)
            "4" -> changeTaskStateUI.execute(tasksStatesIds, tasks)
            else -> {
                viewer.logError("Invalid choice. Please try again.")
                showOptions(projectId, tasksStatesIds, tasks)
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