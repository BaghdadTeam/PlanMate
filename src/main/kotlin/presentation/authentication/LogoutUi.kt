package org.baghdad.presentation.authentication

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import kotlin.system.exitProcess

class  LogoutUi(
    private val useCase: LogoutUseCase,
    private val reader: Reader,
    private val viewer: Viewer,
    private val exit: () -> Unit = { exitProcess(0) }
) {
    fun execute() {
        runBlocking {
            viewer.logMessage("Are you sure you want to logout (y/n)?")
            try {

                val userChoice = reader.readInput()?.lowercase()
                if (userChoice != null) {
                    if (userChoice == "y") {
                        useCase.invoke()
                       exit()
                    }
                }
            } catch (e: LogoutFailedException) {
                viewer.logError("Logout failed: ${e.message}")
            } catch (_: Exception) {
                viewer.logError("Somthing went wrong")
            }
        }
    }
}