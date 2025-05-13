package org.baghdad.presentation.projectStates

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
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

    fun execute(stateId: UUID) {
        val session = sessionManager.currentSession
        val userId = session.userId

        val newTaskStateName = promptForNewStateDetails() ?: return

        tryEditState(stateId, newTaskStateName, userId)
    }


    private fun promptForNewStateDetails(): String? {
        val newTaskStateName = promptForStateName() ?: return null

        return newTaskStateName
    }

    private fun promptForStateName(): String? {
        while (true) {
            viewer.logMessage("Enter the new state name:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }


    private fun tryEditState(stateId: UUID, newTaskStateName: String, userId: UUID) {
        try {
            runBlocking {
                println("This is the new task state name: $newTaskStateName")
                useCase.invoke(stateId, newTaskStateName, userId)
                viewer.logMessage("State updated successfully.")
            }
        } catch (e: Exception) {
            viewer.logError("Failed to update state: ${e.message}")
        }
    }
}