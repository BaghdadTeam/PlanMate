package org.baghdad.presentation.projectStates

import kotlinx.coroutines.runBlocking
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.AddTaskStateToProjectUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class AddTaskStateToProjectUI(
    private val useCase: AddTaskStateToProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute(projectId: UUID) {
        val userId = sessionManager.currentSession.userId


        val state = promptForStateDetails(userId , projectId) ?: return

        tryAddState(state, userId)
    }

    private fun promptForStateDetails(userId: UUID , projectId:UUID): StateEntity? {
        val name = promptForStateName()

        return StateEntity(
            name = name,
            projectId = projectId,
            creatorId = userId
        )
    }

    private fun promptForStateName(): String {
        while (true) {
            viewer.logMessage("Enter the name of the new state:")
            val name = reader.readInput()
            if (!name.isNullOrBlank()) return name
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }


    private fun tryAddState(state: StateEntity, userId: UUID) {
        try {
            runBlocking {
                useCase.invoke(state, userId)
                viewer.logMessage("State '${state.name}' added to project successfully.")
            }
        } catch (_: Exception) {
            viewer.logError("Failed to add state")
        }
    }
}
