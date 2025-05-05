package org.baghdad.presentation.project

import org.baghdad.logic.usecase.common.Result
import org.baghdad.logic.usecase.project.ListProjectsUseCase
import kotlin.collections.orEmpty

class ListProjectUi(
    private val listProjectsUseCase: ListProjectsUseCase
) {


    fun listProjects() {
        println("=== List of Projects ===")
        when (val result = listProjectsUseCase()) {
            is org.baghdad.logic.usecase.common.Result.Failure -> println(" ${result.message}")
            is Result.Success -> {
                val projects = result.data.orEmpty()
                if (projects.isEmpty()) {
                    println("No projects found.")
                    return
                }

                println("+--------------------------------------+----------------------+----------------------+")
                println("| Project ID                           | Name                 | Created By           |")
                println("+--------------------------------------+----------------------+----------------------+")
                for (project in projects) {
                    println("| ${project.id} | ${project.name.padEnd(20)} | ${project.creatorId.padEnd(20)} |")
                }
                println("+--------------------------------------+----------------------+----------------------+")
            }
        }
    }
}