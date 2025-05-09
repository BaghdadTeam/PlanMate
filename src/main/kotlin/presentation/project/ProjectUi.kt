package org.baghdad.presentation.project

import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class ProjectUi(
    private val createProjectUi: CreateProjectUi,
    private val deleteProjectUi: DeleteProjectUi,
    private val editProjectUi: EditProjectUi,
    private val getAllProjectsUi: GetAllProjectsUi,
    private val viewer: Viewer,
    private val reader: Reader
) {
    suspend operator fun invoke(): UUID? {
        while (true) {
            viewer.logMessage("=== Project UI ===")
            viewer.logMessage("1. Create Project")
            viewer.logMessage("2. Delete Project")
            viewer.logMessage("3. Edit Project")
            viewer.logMessage("4. View Project")
            viewer.logMessage("0. Back")
            viewer.logMessage("Enter your choice: ")
            val choice = readln().toIntOrNull()

            when (choice) {
                1 -> createProjectUi.createProject()
                2 -> deleteProjectUi.deleteProject()
                3 -> editProjectUi.editProject()
                4 -> {
                    viewer.logMessage("=== View Projects ===")
                    val ids = getAllProjectsUi()
                    val projectsId = reader.readInput()?.toIntOrNull()
                    if (projectsId != null) {
                        return ids[projectsId]
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