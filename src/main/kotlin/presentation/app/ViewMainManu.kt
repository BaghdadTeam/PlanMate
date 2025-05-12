package org.baghdad.presentation.app

import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.presentation.project.ProjectUi
import org.baghdad.presentation.swimlane.SwimlaneUI
import org.baghdad.presentation.user.UserManagementUI
import kotlin.system.exitProcess

class ViewMainManu(
    private val projectUi: ProjectUi,
    private val userManagementUI: UserManagementUI,
    private val viewer: Viewer,
    private val reader: Reader,
    private val swimlaneUI: SwimlaneUI
) {
    suspend operator fun invoke() {
        while (true) {
            viewer.logMessage("=== Main Menu ===")
            viewer.logMessage("1. View projects")
            viewer.logMessage("2. Create user")
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

                2 -> userManagementUI.invoke()
                0 -> {
                    viewer.logMessage("Goodbye!")
                    exitProcess(0)
                }
            }

        }
    }

}