package org.baghdad.ui.project

import org.baghdad.logic.model.entities.ProjectEntity
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.repositories.ProjectRepository
import org.baghdad.logic.usecase.ProjectsServicesUseCase
import java.util.UUID
import org.baghdad.logic.usecase.common.Result
import java.util.*

class ProjectUi(
    private val projectsServicesUseCase: ProjectsServicesUseCase
) {
    fun createProject(user: UserEntity) {
        println("=== Create Project ===")
        print("Enter project name: ")
        val projectName = readln()

        when (val result = projectsServicesUseCase.create(projectName, user)) {
            is Result.Success -> println("✅ Project \"$projectName\" created successfully.")
            is Result.Failure -> println(" ${result.message}")
        }
    }

    fun editProject(user: UserEntity) {
        println("=== Edit Project ===")
        print("Enter project ID: ")
        val projectIdInput = readln()
        print("Enter new project name: ")
        val newName = readln()

        val projectUUID = runCatching { UUID.fromString(projectIdInput) }.getOrNull()
        if (projectUUID == null) {
            println("Invalid UUID format.")
            return
        }

        when (val result = projectsServicesUseCase.edit(projectUUID, newName, user)) {
            is Result.Success -> println("✅ Project updated successfully.")
            is Result.Failure -> println(" ${result.message}")
        }
    }

    fun listProjects() {
        println("=== List of Projects ===")

        when (val result = projectsServicesUseCase.list()) {
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

    fun deleteProject(user: UserEntity) {
        println("=== Delete Project ===")
        print("Enter project ID: ")
        val projectIdInput = readln()

        val projectUUID = runCatching { UUID.fromString(projectIdInput) }.getOrNull()
        if (projectUUID == null) {
            println(" Invalid UUID format.")
            return
        }

        when (val result = projectsServicesUseCase.delete(projectUUID, user)) {
            is Result.Success -> println("✅ Project deleted successfully.")
            is Result.Failure -> println(" ${result.message}")
        }
    }
}

