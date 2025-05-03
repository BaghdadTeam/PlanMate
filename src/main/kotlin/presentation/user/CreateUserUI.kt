package org.baghdad.presentation.user

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class CreateUserUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val createUser: CreateUserUseCase
) {
    fun run(currentUser: UserEntity?) {
        if (currentUser?.type != UserType.Admin) {
            viewer.logError("🚫 Only administrators can create new users.")
            return
        }

        viewer.logMessage("=== Create New Mate ===")

        val username = prompt("Username: ")
        val name = prompt("Name: ")
        val pass = prompt("Password: ")

        val result = createUser(username, pass, name, currentUser)

        result
            .onSuccess { user ->
                viewer.logMessage("✅ User '${user.username}' created successfully.")
            }
            .onFailure { ex ->
                viewer.logError("⚠️ Error: ${ex.message}")
            }
    }

    private fun prompt(label: String): String {
        viewer.logMessage(label)
        return reader.readInput()?.trim().orEmpty()
    }
}
