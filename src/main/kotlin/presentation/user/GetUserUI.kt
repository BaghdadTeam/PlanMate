package org.baghdad.presentation.user

import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class GetUserUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val getByUsername: GetUserByUsernameUseCase
) {
    fun run() {
        viewer.logMessage("=== Find User ===")
        viewer.logMessage("Username: ")
        val username = reader.readInput()?.trim().orEmpty()

        if (username.isEmpty()) {
            viewer.logMessage(" User '' not found.")
            return
        }
        try {
            val user = getByUsername(username)
            viewer.logMessage(" Found: ${user.username}  Name: ${user.name}  Role: ${user.type}")
        } catch (e: InvalidUsernameException) {
            viewer.logError(" Invalid username: ${e.message}")
        } catch (e: UserNotFoundException) {
            viewer.logError(" ${e.message}")
        }
    }
}
