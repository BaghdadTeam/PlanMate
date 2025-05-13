package org.baghdad.presentation.project

import org.baghdad.logic.usecase.project.GetAllProjectsUseCase
import org.baghdad.presentation.output.Viewer
import java.util.*

class GetAllProjectsUi(
    private val listProjectsUseCase: GetAllProjectsUseCase,
    private val viewer: Viewer,
) {

    suspend operator fun invoke(): Pair<List<UUID>, List<String>> {
        val projects = listProjectsUseCase()
        val projectsUUIDs = mutableListOf<UUID>()
        val projectsName = mutableListOf<String>()

        viewer.logMessage("+----------------------+----------------------+")
        viewer.logMessage("| Project Number       | Name                 |")
        viewer.logMessage("+----------------------+----------------------+")

        projects.forEachIndexed { index, project ->
            viewer.logMessage(
                "| ${(index + 1).toString().padEnd(20)} | ${project.name.padEnd(20)} |"
            )
            projectsUUIDs.add(project.id)
            projectsName.add(project.name)
        }
        viewer.logMessage("+----------------------+----------------------+")

        return projectsUUIDs to projectsName
    }
}
