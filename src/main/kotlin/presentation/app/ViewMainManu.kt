package org.baghdad.presentation.app

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.ProjectManagementUI
import org.baghdad.presentation.swimlane.SwimlaneUI
import org.baghdad.presentation.user.CreateUserUI
import kotlin.system.exitProcess

class ViewMainManu(
    private val projectManagementUI: ProjectManagementUI,
    private val createUserUI: CreateUserUI,
    private val viewer: Viewer,
    private val reader: Reader,
    private val swimlaneUI: SwimlaneUI,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase,
    private val session: SessionManager,
    private val logoutUI: LogoutUi
) {
    suspend operator fun invoke() {
        val userId = session.currentSession.userId

        while (true) {
            viewer.logMessage("=== Main Menu ===")
            viewer.logMessage("1. Project Management")
            viewer.logMessage("2. Logout")
            if (adminPermissionCheckerUseCase(userId)) {
            viewer.logMessage("3. User Management")
            }
            viewer.logMessage("0. Exit")
            viewer.log("Enter your choice: ")
            when (reader.readInput()?.toIntOrNull()) {
                1 -> {
                    val projectId = projectManagementUI.invoke()
                    if (projectId == null) {
                        continue
                    } else {
                        swimlaneUI.invoke(projectId)
                    }
                }

                2->logoutUI.execute()

                3 -> {
                    if (adminPermissionCheckerUseCase(userId)) {
                        createUserUI.invoke()
                    } else {
                        viewer.logMessage("Invalid choice. Please try again.")
                    }
                }

                0 -> {
                    viewer.logMessage("Goodbye!")
                    return
                }

                else -> {
                    viewer.logMessage("Invalid choice. Please try again.")
                }
            }

        }
    }

}