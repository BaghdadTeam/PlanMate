package org.baghdad.presentation.authentication

import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import sun.jvm.hotspot.HelloWorld.e

class LogoutUi(
    private val useCase: LogoutUseCase,
    private val reader: Reader,
    private val viewer: Viewer
) {
    fun execute() {
        viewer.logMessage("Are you sure you want to logout (y)")
        try {


            val userChoice = reader.readInput()?.lowercase()
            if (userChoice != null) {
                if (userChoice == "y") {
                    useCase.invoke()
                }
            }
        } catch (e: LogoutFailedException) {
            viewer.logError("Logout failed: ${e.message}")
        } catch (_: Exception) {
            viewer.logError("Somthing went wrong")
        }
    }
}