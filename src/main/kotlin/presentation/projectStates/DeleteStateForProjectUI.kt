package org.baghdad.presentation.projectStates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.usecase.projectstates.DeleteStateForProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class DeleteStateForProjectUI(
    private val useCase: DeleteStateForProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        val session = sessionManager.currentSession
        val userId = UUID.fromString(session.userId)

        val stateId = promptForStateId() ?: return

        tryDeleteState(stateId, userId)
    }

    private fun promptForStateId(): String? {
        while (true) {
            viewer.logMessage("Enter the ID of the state to delete:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("State ID cannot be blank. Please try again.")
        }
    }

    private fun tryDeleteState(stateId: String, userId: UUID) {
        try {
            useCase.invoke(stateId, userId)
            viewer.logMessage("State deleted successfully.")
        } catch (e: Exception) {
            viewer.logError("Failed to delete state: ${e.message}")
        }
    }
}