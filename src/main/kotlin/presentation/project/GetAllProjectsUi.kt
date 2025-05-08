package org.baghdad.presentation.project

import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetAllProjectsUi(
    private val listProjectsUseCase: GetAllProjectsUseCase,
    private val viewer: Viewer,
) {

    operator fun invoke(): List<UUID> {
        viewer.logMessage("=== List of Projects ===")
        val projects = listProjectsUseCase()
        val projectsUUIDs = mutableListOf<UUID>()

        viewer.logMessage("+----------------------+----------------------+----------------------+")
        viewer.logMessage("| Project ID           | Name                 | Created By           |")
        viewer.logMessage("+----------------------+----------------------+----------------------+")

        projects.forEachIndexed { index, project ->
            viewer.logMessage(
                "| $index | ${project.name.padEnd(20)} | ${
                    project.creatorId.toString().padEnd(20)
                } |"
            )
            projectsUUIDs.add(project.id)
        }
        viewer.logMessage("+----------------------+----------------------+----------------------+")

        return projectsUUIDs
    }
}
