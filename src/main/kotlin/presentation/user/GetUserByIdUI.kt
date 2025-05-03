package org.baghdad.presentation.user

import org.baghdad.logic.usecase.user.GetUserByIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetUserByIdUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val getUserByIdUseCase: GetUserByIdUseCase
) {
    fun run() {
        viewer.logMessage("Enter User ID:")
        val input = reader.readInput()?.trim()
        try {
            val id = UUID.fromString(input)
            val result = getUserByIdUseCase(id)

            result.onSuccess { user ->
                viewer.logMessage("User found:")
                viewer.logMessage("Name: ${user.name}")
                viewer.logMessage("Username: ${user.username}")
                viewer.logMessage("Type: ${user.type}")
            }.onFailure { ex ->
                viewer.logError("Error: ${ex.message}")
            }

        } catch (e: IllegalArgumentException) {
            viewer.logError("Invalid UUID format.")
        }
    }
}
