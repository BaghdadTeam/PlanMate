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
    suspend fun invoke(projectId: UUID) {
        try {
            val userId = session.currentSession.userId
            val swimLane = renderSwimlaneUI.invoke(projectId)

            while (true) {
                viewer.logMessage("=== Plan Mate ===")
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
                        viewer.logMessage("Navigating to Tasks Screen...")
                        taskUI.execute(projectId)
                    }

                    2 -> {
                        viewer.logMessage("Navigating to Audit Log Screen...")
                        auditUI(projectId)
                    }

                    3 -> {
                        if (adminPermissionCheckerUseCase(userId)) {
                            viewer.logMessage("Navigating to Project States Screen...")
                            projectStatesUI.invoke(projectId)}
                        else{
                            viewer.logMessage("Invalid choice. Please try again.")
                        }
                    }

                    0 -> {
                        viewer.logMessage("Returning to the previous screen...")
                        return
                    }

                    null -> {
                        viewer.logMessage("Invalid input. Please enter a number.")
                    }

                    else -> {
                        viewer.logMessage("Invalid choice. Please try again.")
                    }
                }
                viewer.logMessage("")
            }
        } catch (exception: Exception) {
            viewer.logError("Error: ${exception.message}")
        }
    }
}
