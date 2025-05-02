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
            val result = sessionManger.getActiveSession()
            val session = result.getOrElse { exception ->
                viewer.logMessage("Failed to load session: ${exception.message}")
                val newSession = loginUi.execute()
                newSession

            }
            sessionManger.setSession(session)

        } catch (e: SessionNotFoundException) {
            viewer.logError(e.message ?: "No session found")
        } catch (e: Exception) {
            viewer.logError(e.message ?: "Something went wrong")
        }
    }
}