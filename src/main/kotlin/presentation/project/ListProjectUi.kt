package org.baghdad.presentation.project

import org.baghdad.logic.usecase.project.GetAllProjectsUseCase

class ListProjectUi(
    private val listProjectsUseCase: GetAllProjectsUseCase
) {


    suspend fun listProjects() {
        println("=== List of Projects ===")
        val projects = listProjectsUseCase()
        if (projects.isEmpty()) {
            println("No projects found.")
            return
        }

        println("+--------------------------------------+----------------------+----------------------+")
        println("| Project ID                           | Name                 | Created By           |")
        println("+--------------------------------------+----------------------+----------------------+")
        for (project in projects) {
            println(
                "| ${project.id} | ${project.name.padEnd(20)} | ${
                    project.creatorId.toString().padEnd(20)
                } |"
            )
        }
        println("+--------------------------------------+----------------------+----------------------+")
    }
}
