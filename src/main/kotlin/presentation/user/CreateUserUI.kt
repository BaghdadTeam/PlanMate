package org.baghdad.presentation.user

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.InvalidNameException
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.InvalidUsernameException
import org.baghdad.logic.model.exceptions.UnauthorizedException
import org.baghdad.logic.model.exceptions.UserAlreadyExistsException
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
        viewer.log("Username: ")
        val username = reader.readInput()
        viewer.log("Name: ")
        val name = reader.readInput()
        viewer.log("Password: ")
        val password = reader.readInput()


        try {
            if (username != null && name != null && password != null) {
                val newUser = createUser(username, password, name, userId)
                viewer.logMessage("User '${newUser.username}' created successfully.")
            } else {
                viewer.logError("Invalid input. Please try again.")
            }
        } catch (_: InvalidUsernameException) {
            viewer.logError("Invalid username")
        } catch (_: InvalidNameException) {
            viewer.logError("Invalid name")
        } catch (_: InvalidPasswordException) {
            viewer.logError("Invalid password")
        } catch (_: UserAlreadyExistsException) {
            viewer.logError("Username already exists")
        } catch (e: UnauthorizedException) {
            viewer.logError("${e.message}")
        }
    }
}