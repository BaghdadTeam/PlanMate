package org.baghdad.presentation.app

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.presentation.authentication.LogoutUi
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.ProjectUi
import org.baghdad.presentation.swimlane.SwimlaneUI
import org.baghdad.presentation.user.CreateUserUI
import kotlin.system.exitProcess

class ViewMainManu(
    private val projectUi: ProjectUi,
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
            viewer.logMessage("1. View projects")
            viewer.logMessage("2. Create user")
            viewer.logMessage("3. Logout")

            if (adminPermissionCheckerUseCase(userId)) {
                viewer.logMessage("2. Create user")
            }

            viewer.logMessage("0. Exit")
            when (reader.readInput()?.toIntOrNull()) {
                1 -> {
                    val projectId = projectUi.invoke()
                    if (projectId == null) {
                        continue
                    } else {
                        swimlaneUI.invoke(projectId)
                    }
                }

                2 -> {
                    if (adminPermissionCheckerUseCase(userId)) {
                        createUserUI.invoke()
                    } else {
                        viewer.logMessage("Invalid choice. Please try again.")
                    }
                }
                3->logoutUI.execute()
                0 -> {
                    viewer.logMessage("Goodbye!")
                    exitProcess(0)
                }

                else -> {
                    viewer.logMessage("Invalid choice. Please try again.")
                }
            }

        }
    }

}