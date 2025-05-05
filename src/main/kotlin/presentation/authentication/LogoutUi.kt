package org.baghdad.presentation.authentication

import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.presentation.app.Feature
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class LogoutUi(
    private val useCase: LogoutUseCase,
    private val reader: Reader,
    private val viewer: Viewer
) : Feature {
    override val id: Int = FEATURE_ID
    override val name: String = FEATURE_NAME
    override fun execute() {
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
            viewer.logError("Something went wrong")
        }
    }

    companion object {
        const val FEATURE_ID = 0
        const val FEATURE_NAME = "Logout"
    }
}