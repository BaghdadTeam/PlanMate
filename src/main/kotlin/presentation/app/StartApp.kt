package org.baghdad.presentation.app

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.output.Viewer

class StartApp(
    private val loginUi: LoginUi,
    private val sessionManager: SessionManager,
    private val viewer: Viewer
) {
    fun run() {
        try {
            sessionManager.clearExpiredSession()
            val session = try {
                sessionManager.getActiveSession()
            } catch (e: SessionNotFoundException) {
                viewer.logMessage("No active session found, starting login...")
                loginUi.execute()
            }
            sessionManager.setSession(session)
        } catch (e: Exception) {
            viewer.logError("Something went wrong: ${e.message}")
        }
    }
}