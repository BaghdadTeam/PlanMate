package org.baghdad.presentation.app

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.presentation.authentication.LoginUi
import org.baghdad.presentation.output.Viewer

class StartApp(
    private val loginUi: LoginUi,
    private val sessionManger: SessionManager,
    private val viewer: Viewer
) {
    fun run() {
        try {
            sessionManger.clearExpiredSession()
            val session = try {
                val result = sessionManger.getActiveSession()
                result.getOrElse { exception ->
                    viewer.logMessage("Failed to load session: ${exception.message}")
                    loginUi.execute()
                }
            } catch (e: SessionNotFoundException) {
                viewer.logMessage("No active session found, starting login...")
                loginUi.execute()
            }
            sessionManger.setSession(session)
        } catch (e: Exception) {
            viewer.logError("Something went wrong: ${e.message}")
        }
    }
}