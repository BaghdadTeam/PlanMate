package org.baghdad.presentation.project

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ProjectUi(
    private val createProjectUi: CreateProjectUi,
    private val deleteProjectUi: DeleteProjectUi,
    private val editProjectUi: EditProjectUi,
    private val getAllProjectsUi: GetAllProjectsUi,
    private val session: SessionManager,
    private val adminPermissionCheckerUseCase: AdminPermissionCheckerUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {
    suspend operator fun invoke(): UUID? {
        val userId = session.currentSession.userId
        while (true) {
            viewer.logMessage("=== Project UI ===")
            viewer.logMessage("1. View Project")

            if (adminPermissionCheckerUseCase(userId)) {
                viewer.logMessage("2. Create Project")
                viewer.logMessage("3. Delete Project")
                viewer.logMessage("4. Edit Project")
            }

            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")
            val choice = reader.readInput()?.toIntOrNull()

            when (choice) {
                1 -> {
                    if (adminPermissionCheckerUseCase(userId)) {
                        createProjectUi.createProject()
                    } else {
                        viewer.logError("Invalid choice. Please try again.")
                    }
                }

                2 -> {
                    if (adminPermissionCheckerUseCase(userId)) {
                        deleteProjectUi.deleteProject()
                    } else {
                        viewer.logError("Invalid choice. Please try again.")
                    }
                }

                3 -> if (adminPermissionCheckerUseCase(userId)) {
                    editProjectUi.editProject()
                } else {
                    viewer.logError("Invalid choice. Please try again.")
                }

                4 -> {
                    viewer.logMessage("=== View Projects ===")
                    val ids = getAllProjectsUi()
                    viewer.logAuth("Enter project id: ")
                    val projectsId = reader.readInput()?.toIntOrNull()
                    if (projectsId != null) {
                        return ids[projectsId - 1]
                    } else {
                        viewer.logError("Project Id should be a number")
                    }
                }

                0 -> return null
                else -> viewer.logError("Invalid choice. Please try again.")
            }
        }
    }
}