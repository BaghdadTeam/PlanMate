package org.baghdad.presentation.swimlane

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.presentation.audit.AuditUI
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.projectStates.ProjectStatesUI
import org.baghdad.presentation.task.TaskManagementGatherUI
import java.util.UUID

class SwimlaneUI(
    private val renderSwimlaneUI: RenderSwimlaneUI,
    private val projectStatesUI: ProjectStatesUI,
    private val taskUI: TaskManagementGatherUI,
    private val auditUI: AuditUI,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase,
    private val session: SessionManager,
    private val reader: Reader,
    private val viewer: Viewer

) {
    suspend fun invoke(project: Pair<UUID, String>) {
        try {
            val swimLane = renderSwimlaneUI.invoke(project.first)
            val userId = session.currentSession.userId

            while (true) {
                viewer.logMessage("=== ${project.second} ===")
                viewer.logMessage(swimLane)
                viewer.logMessage("1. Manage Tasks")
                viewer.logMessage("2. View Audit Log")

                if (adminPermissionCheckerUseCase(userId)) {
                    viewer.logMessage("3. Manage States (Admin Only)")
                }
                viewer.logMessage("0. Back to Previous Screen")

                print("Enter your choice: ")
                when (reader.readInput()?.toIntOrNull()) {
                    1 -> {
                        println("Navigating to Tasks Screen...")
                        taskUI.execute(project.first)
                    }

                    2 -> {
                        println("Navigating to Audit Log Screen...")
                        auditUI.invoke(project.first)
                    }

                    3 -> {
                        if (adminPermissionCheckerUseCase(userId)) {
                            viewer.logMessage("Navigating to Project States Screen...")
                            projectStatesUI.invoke(project.first)}
                        else{
                            viewer.logMessage("Invalid choice. Please try again.")
                        }
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
