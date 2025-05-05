package org.baghdad.presentation.projectStates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.EditProjectStatesUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class EditProjectStateUI(
    private val useCase: EditProjectStatesUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute(projectId: UUID) {
        val session = sessionManager.currentSession
        val userId = UUID.fromString(session.userId)

        val stateId = promptForStateId() ?: return
        val newState = promptForNewStateDetails(userId, projectId) ?: return

        tryEditState(UUID.fromString(stateId), newState, userId)
    }

    private fun promptForStateId(): String? {
        while (true) {
            viewer.logMessage("Enter the ID of the state to edit:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("State ID cannot be blank. Please try again.")
        }
    }

    private fun promptForNewStateDetails(userId: UUID, projectId: UUID): StateEntity? {
        val name = promptForStateName() ?: return null

        return StateEntity(
            name = name,
            projectId = projectId,
            creatorId = userId
        )
    }

    private fun promptForStateName(): String? {
        while (true) {
            viewer.logMessage("Enter the new state name:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }


    private fun tryEditState(stateId: UUID, newState: StateEntity, userId: UUID) {
        try {
            useCase.invoke(stateId, newState, userId)
            viewer.logMessage("State updated successfully.")
        } catch (e: Exception) {
            viewer.logError("Failed to update state: ${e.message}")
        }
    }
}