package org.baghdad.presentation.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.model.exceptions.UserCanNotBeFoundException
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class LoginUi(
    private val useCase: LoginUseCase,
    private val viewer: Viewer,
    private val reader: Reader,
) {
    suspend fun execute(): SessionEntity {
        while (true) {
            viewer.logMessage("Please enter username:")
            val username = reader.readInput()
            viewer.logMessage("Please enter password:")
            val password = reader.readInput()

            if (username != null && password != null) {
                try {
                    val session = useCase(username, password)
                    viewer.logMessage("Successfully logged in as $username")
                    return session
                } catch (e: InvalidCredentialsException) {
                    viewer.logMessage("Login failed: ${e.message}")
                } catch (e: UserCanNotBeFoundException) {
                    viewer.logMessage("Login failed ${e.message}")
                } catch (e: Exception) {
                    viewer.logMessage("Unexpected error: ${e.message}")
                }
            } else {
                viewer.logMessage("Username and password cannot be null")
            }
        }
    }
}

