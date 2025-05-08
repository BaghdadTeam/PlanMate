package org.baghdad.presentation.authentication

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.model.exceptions.UserCanNotBeFoundException

class LoginUi(
    private val useCase: LoginUseCase,
    private val viewer: Viewer,
    private val reader: Reader,
) {
    fun execute(): SessionEntity {
            while (true) {
                viewer.logMessage("Please enter username:")
                val username = reader.readInput()
                viewer.logMessage("Please enter password:")
                val password = reader.readInput()

                if (username != null && password != null) {
                    try {
                        runBlocking {
                            val session = useCase(username, password)
                            viewer.logMessage("Successfully logged in as $username")
                            return@runBlocking session
                        }
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

