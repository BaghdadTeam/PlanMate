package org.baghdad.presentation.swimlane

import org.baghdad.logic.usecase.ViewServiceUseCase
import org.baghdad.presentation.audit.AuditManagementUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.ProjectStatesManagementUI
import org.baghdad.presentation.task.TaskManagementUI
import java.util.*

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesManagementUI: ProjectStatesManagementUI,
    private val taskUI: TaskManagementUI,
    private val auditManagementUI: AuditManagementUI,
    private val reader: Reader,
    private val viewer: Viewer,
    private val viewServiceUseCase: ViewServiceUseCase
) {
    suspend fun invoke(project: Pair<UUID, String>) {
        try {
            while (true) {
                val projectData =try {
                  viewServiceUseCase.invoke(project.first)
                }catch (_:Exception){
                    emptyMap()
                }
                val swimLane= renderSwimlaneUI.invoke(projectData)
                viewer.logMessage("=== Plan Mate ===")
                viewer.logMessage(swimLane)
                viewer.logMessage("1. Manage States (Admin Only)")
                viewer.logMessage("2. Manage Tasks")
                viewer.logMessage("3. View Audit Log")
                viewer.logMessage("0. Back to Previous Screen")

                print("Enter your choice: ")
                when (reader.readInput()?.toIntOrNull()) {
                    1 -> {
                        println("Navigating to Project States Screen...")
                        projectStatesManagementUI.invoke(project.first)
                    }

                    2 -> {
                        println("Navigating to Tasks Screen...")
                        taskUI.execute(projectData , project.first)
                    }

                    3 -> {
                        println("Navigating to Audit Log Screen...")
                        auditManagementUI(project.first)
                    }

                    0 -> {
                        println("Returning to the previous screen...")
                        return
                    }

                    null -> {
                        println("Invalid input. Please enter a number.")
                    }

                    else -> {
                        println("Invalid choice. Please try again.")
                    }
                }
                println()
            }
        } catch (exception: Exception) {
            viewer.logError("Error: ${exception.message}")
        }
    }
}
