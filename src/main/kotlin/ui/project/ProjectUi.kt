package org.baghdad.ui.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import java.util.UUID
import org.baghdad.logic.usecase.common.Result
import org.baghdad.logic.usecase.project.CreateProjectUseCase
import org.baghdad.logic.usecase.project.DeleteProjectUseCase
import org.baghdad.logic.usecase.project.EditProjectUseCase
import org.baghdad.logic.usecase.project.ListProjectsUseCase
import java.util.*

class ProjectUi(
    private val createProjectUseCase: CreateProjectUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val listProjectsUseCase: ListProjectsUseCase
) {

    fun createProject(user: UserEntity) {
        println("=== Create Project ===")
        print("Enter project name: ")
        val name = readln()

        when (val result = createProjectUseCase(name, user)) {
            is Result.Success -> println("Project \"$name\" created successfully.")
            is Result.Failure -> println(result.message)
        }
    }

    fun editProject(user: UserEntity) {
        println("=== Edit Project ===")
        print("Enter project ID: ")
        val idInput = readln()
        print("Enter new project name: ")
        val newName = readln()

        val projectId = idInput.toUUIDOrNull()
        if (projectId == null) {
            println("Invalid UUID format.")
            return
        }

        when (val result = editProjectUseCase(projectId, newName, user)) {
            is Result.Success -> println("Project updated successfully.")
            is Result.Failure -> println(result.message)
        }
    }

    fun deleteProject(user: UserEntity) {
        println("=== Delete Project ===")
        print("Enter project ID: ")
        val idInput = readln()

        val projectId = idInput.toUUIDOrNull()
        if (projectId == null) {
            println("Invalid UUID format.")
            return
        }

        when (val result = deleteProjectUseCase(projectId, user)) {
            is Result.Success -> println("Project deleted successfully.")
            is Result.Failure -> println(result.message)
        }
    }

    fun listProjects() {
        println("=== List of Projects ===")
        when (val result = listProjectsUseCase()) {
            is Result.Failure -> println(" ${result.message}")
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

    private fun String.toUUIDOrNull(): UUID? =
        runCatching { UUID.fromString(this) }.getOrNull()
}

