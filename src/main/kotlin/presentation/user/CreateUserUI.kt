package org.baghdad.presentation.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.usecase.user.CreateUserUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class CreateUserUI(
    private val reader: Reader,
    private val viewer: Viewer,
    private val createUser: CreateUserUseCase,
    private val session : SessionManager
) {
    suspend operator fun invoke() {
        val userId = session.currentSession.userId
        viewer.logMessage("=== Create New Mate ===")
        viewer.logAuth("Username: ")
        val username = reader.readInput()
        viewer.logAuth("Name: ")
        val name = reader.readInput()
        viewer.logAuth("Password: ")
        val password = reader.readInput()


        try {
            if (username != null && name != null && password != null) {
                val newUser = createUser(username, password, name, userId)
                viewer.logMessage("User '${newUser.username}' created successfully.")
            } else {
                viewer.logError("Invalid input. Please try again.")
            }
        } catch (e: InvalidUsernameException) {
            viewer.logError("Invalid username: ${e.message}")
        } catch (e: InvalidNameException) {
            viewer.logError("Invalid name: ${e.message}")
        } catch (e: InvalidPasswordException) {
            viewer.logError("Invalid password: ${e.message}")
        } catch (e: UserAlreadyExistsException) {
            viewer.logError("${e.message}")
        } catch (e: UnauthorizedException) {
            viewer.logError("${e.message}")
        }
    }


    private fun prompt(label: String): String {
        viewer.logMessage(label)
        return reader.readInput()?.trim().orEmpty()
    }
}