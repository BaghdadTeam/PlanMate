package org.baghdad.presentation.app

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.output.Viewer

class StartApp(
    private val loginUi: LoginUi,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val viewMainManu: ViewMainManu
) {
    fun run() {
        runBlocking {
            try {
                val session = try {
                    val activeSession = sessionManager.getActiveSession()
                    viewer.logMessage("Your session is still active, welcome back!")
                    activeSession
                } catch (_: SessionNotFoundException) {
                    viewer.logMessage("No active session found, starting login...")
                    loginUi.execute()
                }
                sessionManager.setSession(session)
                viewMainManu()
            } catch (e: Exception) {
                viewer.logError("Something went wrong: ${e.message}")
            }
        }
    }
}