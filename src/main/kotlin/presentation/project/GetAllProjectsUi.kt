package org.baghdad.presentation.project

import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetAllProjectsUi(
    private val listProjectsUseCase: GetAllProjectsUseCase,
    private val viewer: Viewer,
) {

    suspend operator fun invoke(): List<UUID> {
        viewer.logMessage("=== List of Projects ===")
        val projects = listProjectsUseCase()
        val projectsUUIDs = mutableListOf<UUID>()

        viewer.logMessage("+----------------------+----------------------+")
        viewer.logMessage("| Project Number       | Name                 |")
        viewer.logMessage("+----------------------+----------------------+")

        projects.forEachIndexed { index, project ->
            viewer.logMessage(
                "| ${(index + 1).toString().padEnd(20)} | ${project.name.padEnd(20)} |"
            )
            projectsUUIDs.add(project.id)
        }
        viewer.logMessage("+----------------------+----------------------+")

        return projectsUUIDs
    }
}
