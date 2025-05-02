package org.baghdad.presentation.authentication

import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class LogoutUi(
    private val useCase: LogoutUseCase,
    private val reader: Reader,
    private val viewer: Viewer
) {
    fun execute() {
        viewer.logMessage("Are you sure you want to logout (y)")
        val userChoice = reader.readInput()?.lowercase()
        if (userChoice != null ) {
            if(userChoice == "y") {
                useCase.invoke()
            }
        }
    }
}