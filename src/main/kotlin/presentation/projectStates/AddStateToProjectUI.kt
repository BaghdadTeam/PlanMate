package org.baghdad.presentation.projectStates

import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.StateEntity
import org.baghdad.logic.usecase.projectstates.AddStateToProjectUseCase
import org.baghdad.presentation.app.Feature
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class AddStateToProjectUI(
    private val useCase: AddStateToProjectUseCase,
    private val sessionManager: SessionManager,
    private val viewer: Viewer,
    private val reader: Reader
) : Feature {
    override val id: Int = 6
    override val name: String = "Add State to Project"

    override fun execute() {
        val session = sessionManager.currentSession

        val userId = session.userId

        val state = promptForStateDetails(userId) ?: return

        tryAddState(state, userId)
    }

    private fun promptForStateDetails(userId: UUID): StateEntity? {
        val name = promptForStateName() ?: return null
        val projectId = promptForProjectId() ?: return null

        return StateEntity(
            name = name,
            projectId = UUID.fromString(projectId),
            creatorId = userId
        )
    }

    private fun promptForStateName(): String? {
        while (true) {
            viewer.logMessage("Enter the name of the new state:")
            val name = reader.readInput()
            if (!name.isNullOrBlank()) return name
            viewer.logError("State name cannot be blank. Please try again.")
        }
    }

    private fun promptForProjectId(): String? {
        while (true) {
            viewer.logMessage("Enter the project ID for the state:")
            val projectId = reader.readInput()
            if (!projectId.isNullOrBlank()) return projectId
            viewer.logError("Project ID cannot be blank. Please try again.")
        }
    }

    private fun tryAddState(state: StateEntity, userId: UUID) {
        try {
            useCase.invoke(state, userId)
            viewer.logMessage("State '${state.name}' added to project successfully.")
        } catch (e: Exception) {
            viewer.logError("Failed to add state: ${e.message}")
        }
    }
}
