package org.baghdad.presentation.projectStates

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class DeleteStateForProjectUI(
    private val useCase: DeleteStateForProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
) {

    fun execute(stateId: UUID) {
        val userId = sessionManager.currentSession.userId


        tryDeleteState(stateId, userId)
    }

    private fun tryDeleteState(stateId: UUID, userId: UUID) {
        try {
            runBlocking {
                useCase.invoke(stateId, userId)
                viewer.logMessage("State deleted successfully.")
            }
        } catch (e: Exception) {
            viewer.logError("Failed to delete state: ${e.message}")
        }
    }
}