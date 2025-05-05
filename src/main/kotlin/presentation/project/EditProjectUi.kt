package org.baghdad.presentation.project

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.usecase.common.Result
import org.baghdad.logic.usecase.project.EditProjectUseCase
import java.util.*

fun String.toUUIDOrNull(): UUID? =
    runCatching { UUID.fromString(this) }.getOrNull()

class EditProjectUi(
    private val editProjectUseCase: EditProjectUseCase
) {
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
}