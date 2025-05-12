package org.baghdad.presentation.audit
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.task.GetTasksByProjectIdUI
import java.util.UUID

class AuditUI(
    private val showAuditByProjectIdUI: ShowAuditByProjectIdUI,
    private val showAuditByTaskIdUI: ShowAuditByTaskIdUI,
    private val getTasksByProjectIdUI: GetTasksByProjectIdUI,
    private val viewer: Viewer,
    private val reader: Reader
) {
    operator fun invoke(projectId: UUID) {
        while (true) {
            viewer.logMessage("=== Audit UI ===")
            viewer.logMessage("Selected Project ID: $projectId")
            viewer.logMessage("1. Show audit for this project")
            viewer.logMessage("2. Show audit by Task ID")
            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")

            val choice = readln().toIntOrNull()

            when (choice) {
                1 -> showAuditByProjectIdUI.execute(projectId)

                2 -> {
                    val taskIds = getTasksByProjectIdUI.execute(projectId)
                    if (taskIds.isEmpty()) {
                        viewer.logMessage("No tasks available to audit.")
                        return
                    }

                    viewer.logMessage("Select a task by number:")
                    val input = reader.readInput()
                    val index = input?.toIntOrNull()

                    if (index != null && index in 1..taskIds.size) {
                        val selectedTaskId = taskIds[index - 1]
                        showAuditByTaskIdUI.execute(selectedTaskId)
                    } else {
                        viewer.logError("Invalid selection. Please enter a valid task number.")
                    }
                }

                0 -> return
                else -> viewer.logError("Invalid choice. Please try again.")
            }
        }
    }
}
