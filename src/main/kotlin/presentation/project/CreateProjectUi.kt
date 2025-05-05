package org.baghdad.presentation.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.usecase.common.Result
import org.baghdad.logic.usecase.project.CreateProjectUseCase

class CreateProjectUi(
    private val createProjectUseCase: CreateProjectUseCase
) {
    fun createProject(user: UserEntity) {
        println("=== Create Project ===")
        print("Enter project name: ")
        val name = readln()

        when (val result = createProjectUseCase(name, user)) {
            is org.baghdad.logic.usecase.common.Result.Success -> println("Project \"$name\" created successfully.")
            is Result.Failure -> println(result.message)
        }
    }
}