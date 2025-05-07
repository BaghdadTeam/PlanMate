package org.baghdad.presentation.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.usecase.project.DeleteProjectUseCase

class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectUseCase
) {
    fun deleteProject(user: UserEntity) {
        println("=== Delete Project ===")
        print("Enter project ID: ")
        val idInput = readln()

        val projectId = idInput.toUUIDOrNull()
        if (projectId == null) {
            println("Invalid UUID format.")
            return
        }

//        when (val result = deleteProjectUseCase(projectId, user)) {
//            is org.baghdad.logic.usecase.common.Result.Success -> println("Project deleted successfully.")
//            is Result.Failure -> println(result.message)
//        }
    }
}