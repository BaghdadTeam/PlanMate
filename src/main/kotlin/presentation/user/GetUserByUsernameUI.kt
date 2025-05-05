package org.baghdad.presentation.user

import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class GetUserByUsernameUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val getByUsername: GetUserByUsernameUseCase
) {
    fun run() {
        viewer.logMessage("=== Find User ===")
        viewer.logMessage("Please enter the username:")
        val username = reader.readInput()?.trim().orEmpty()

        try {
            val user = getByUsername(username)
            viewer.logMessage("Found: ${user.username}  Name: ${user.name}  Role: ${user.type}")
        } catch (exception: InvalidUsernameException) {
            viewer.logError("Invalid username: ${exception.message}")
        } catch (exception: UserNotFoundException) {
            viewer.logError(exception.message ?: "User not found.")
        }
    }
}

//