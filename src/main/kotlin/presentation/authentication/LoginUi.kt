package org.baghdad.presentation.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

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
                val result = useCase(username, password)
                result.onSuccess {
                    viewer.logMessage("Successfully logged in as $username")
                    return it
                }.onFailure {
                    viewer.logMessage("Login failed: ${it.message}")
                }
            } else {
                viewer.logMessage("Username and password cannot be null")
            }
        }
    }
}