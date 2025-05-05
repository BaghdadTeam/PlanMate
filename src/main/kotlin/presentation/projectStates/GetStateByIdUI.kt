package org.baghdad.presentation.projectStates

import org.baghdad.logic.usecase.projectstates.GetStateByIdUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer
import java.util.UUID

class GetStateByIdUI(
    private val useCase: GetStateByIdUseCase,
    private val viewer: Viewer,
    private val reader: Reader
) {

    fun execute() {
        val stateId = promptForStateId() ?: return

        try {
            val state = useCase.invoke(UUID.fromString(stateId))
            viewer.logMessage("State: ${state?.name}")
        } catch (e: Exception) {
            viewer.logError("Failed to retrieve state: ${e.message}")
        }
    }

    private fun promptForStateId(): String? {
        while (true) {
            viewer.logMessage("Enter the state ID:")
            val input = reader.readInput()
            if (!input.isNullOrBlank()) return input
            viewer.logError("State ID cannot be blank. Please try again.")
        }
    }
}